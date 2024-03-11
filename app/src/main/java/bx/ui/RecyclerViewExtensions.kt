package bx.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bx.util.inflate

internal class RecyclerHolder(it: View) : RecyclerView.ViewHolder(it)

internal class RecyclerDelegate<T : Any, H : RecyclerView.ViewHolder>(
    var type: (T) -> Int,
    var create: (ViewGroup, type: Int) -> H,
    var bind: H.(T) -> Unit,
    var sameContent: (T, T) -> Boolean = { old, new -> old == new },
    var sameItem: (T, T) -> Boolean = { old, new -> sameContent(old, new) },
    var onUpdated: () -> Unit,
) : ListAdapter<T, H>(object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = sameItem(oldItem, newItem)
    override fun areContentsTheSame(oldItem: T, newItem: T) = sameContent(oldItem, newItem)
}) {
    fun update(it: List<T>) = submitList(it, onUpdated)
    override fun getItemViewType(position: Int): Int = type(getItem(position))
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = (create(parent, viewType))
    override fun onBindViewHolder(holder: H, position: Int) = holder.bind(getItem(position))
}

fun <T : Any> RecyclerView.recycler(
    data: List<T>,
    type: (T) -> Int,
    create: (ViewGroup, type: Int) -> View = { parent, type -> parent.inflate(type) },
    sameContent: (T, T) -> Boolean = { old, new -> old == new },
    sameItem: (T, T) -> Boolean = { old, new -> sameContent(old, new) },
    onUpdated: () -> Unit = {},
    bind: View.(T) -> Unit,
): RecyclerView = apply {
    val holderCreate = { parent: ViewGroup, type: Int -> RecyclerHolder(create(parent, type)) }
    val holderBind: RecyclerHolder.(T) -> Unit = { itemView.bind(it) }
    if (adapter == null) adapter = RecyclerDelegate(
        type, holderCreate, holderBind, sameContent, sameItem, onUpdated
    )
    @Suppress("UNCHECKED_CAST")
    (adapter as RecyclerDelegate<T, RecyclerHolder>).let {
        it.type = type
        it.create = holderCreate
        it.onUpdated = onUpdated
        it.bind = holderBind
        it.update(data)
    }
}
