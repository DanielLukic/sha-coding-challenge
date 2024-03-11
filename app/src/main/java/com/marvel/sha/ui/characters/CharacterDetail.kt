package com.marvel.sha.ui.characters

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bx.logging.Log
import bx.ui.recycler
import coil.compose.AsyncImage
import com.marvel.sha.R
import com.marvel.sha.data.MarvelUrl
import com.marvel.sha.data.string
import com.marvel.sha.ui.IndeterminateProgress
import com.marvel.sha.ui.ShaTopBar
import org.koin.androidx.compose.koinViewModel

// of course a lot of room for improvement here..
// but the only goal of this was to use the recycler view..
// because this was a test requirement..

@OptIn(ExperimentalLayoutApi::class)
@Composable internal fun CharacterDetail(
    modifier: Modifier = Modifier,
    model: CharacterDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {

    val detail = model.observe().collectAsState().value.valueOrNull()
    val title = detail?.name ?: stringResource(id = R.string.app_loading)

    Scaffold(
        modifier = modifier,
        topBar = { ShaTopBar(title = title, onBackClick = onBackClick) }
    ) { contentPadding ->
        Surface(modifier = Modifier.padding(contentPadding)) {
            if (detail == null) {
                IndeterminateProgress()
            }
            else AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { RecyclerView(it).apply { layoutManager = LinearLayoutManager(it) } },
            ) {

                val data = listOf(
                    Entry.Image(detail.thumbnail.url),
                    Entry.WebHooks(detail.urls),
                    Entry.Section("Comics"),
                    Entry.CrossHooks(toItems(detail.comics)),
                    Entry.Section("Stories"),
                    Entry.CrossHooks(toItems(detail.stories)),
                    Entry.Section("Events"),
                    Entry.CrossHooks(toItems(detail.events)),
                    Entry.Section("Series"),
                    Entry.CrossHooks(toItems(detail.series)),
                )

                it.recycler(
                    data = data,
                    type = Entry::type,
                    create = { parent, _ -> ComposeView(parent.context) },
                ) {
                    (this as ComposeView).setContent {
                        when (it) {
                            is Entry.Section    -> Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                text = it.title
                            )
                            is Entry.Image      -> AsyncImage(
                                modifier = Modifier.fillMaxWidth(),
                                model = it.url,
                                contentDescription = "Image"
                            )
                            is Entry.WebHooks   -> FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                it.urls.forEach { entry ->
                                    Button(
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        ),
                                        onClick = { Log.info(entry.url) } // TODO do something here
                                    ) {
                                        Text(text = entry.type)
                                    }
                                }
                            }
                            is Entry.CrossHooks -> FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                it.items.forEach { entry ->
                                    Button(
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        ),
                                        onClick = { Log.info(entry) } // TODO do something here
                                    ) {
                                        Text(text = entry)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
private fun toItems(it: Map<String, Any>): List<String> {
    val items = it["items"] as List<Map<String, Any>>
    return items.map { it.string("name") }
}

private sealed interface Entry {

    val type: Int

    class Section(val title: String, override val type: Int = TYPE_SECTION) : Entry
    class Image(val url: String, override val type: Int = TYPE_IMAGE) : Entry
    class WebHooks(val urls: List<MarvelUrl>, override val type: Int = TYPE_WEB_HOOK) : Entry
    class CrossHooks(val items: List<String>, override val type: Int = TYPE_CROSS_HOOK) : Entry
}

private const val TYPE_SECTION = 1
private const val TYPE_IMAGE = TYPE_SECTION + 1
private const val TYPE_WEB_HOOK = TYPE_IMAGE + 1
private const val TYPE_CROSS_HOOK = TYPE_WEB_HOOK + 1
