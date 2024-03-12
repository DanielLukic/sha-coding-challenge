package com.marvel.sha.ui.comics

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.marvel.sha.domain.MarvelCollection
import com.marvel.sha.ui.IndeterminateProgress
import com.marvel.sha.ui.ShaTopBar
import org.koin.androidx.compose.koinViewModel

// of course a lot of room for improvement here..
// but the only goal of this was to use the recycler view..
// because this was a test requirement..

@Composable internal fun ComicDetailScreen(
    modifier: Modifier = Modifier,
    model: ComicDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {

    val detail = model.observe().collectAsState().value.valueOrNull()
    val title = detail?.title ?: stringResource(id = R.string.app_loading)

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
            ) { rv ->

                val data = listOf(
                    Entry.Image(detail.thumbnail.url),
                    Entry.WebHooks(detail.urls),
                    Entry.Section("Characters"),
                    Entry.CrossHooks(toItems(detail.characters)),
                    Entry.Section("Stories"),
                    Entry.CrossHooks(toItems(detail.stories)),
                    Entry.Section("Events"),
                    Entry.CrossHooks(toItems(detail.events)),
                    Entry.Section("Creators"),
                    Entry.CrossHooks(toItems(detail.creators)),
                )

                rv.recycler(
                    data = data,
                    type = Entry::type,
                    create = { parent, _ -> ComposeView(parent.context) },
                ) { entry ->
                    (this as ComposeView).setContent {
                        when (entry) {
                            is Entry.Image -> Image(entry)
                            is Entry.CrossHooks -> FlowItems(items = entry.items)
                            is Entry.WebHooks -> FlowItems(items = entry.urls.map { it.type })
                            is Entry.Section -> Section(entry)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Image(entry: Entry.Image) = AsyncImage(
    modifier = Modifier.fillMaxWidth(),
    model = entry.url,
    contentDescription = "Image"
)

@Composable
private fun Section(it: Entry.Section, color: Color = Color.Yellow) = Text(
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp),
    style = MaterialTheme.typography.titleMedium,
    color = color,
    textAlign = TextAlign.Center,
    text = it.title
)

@Composable @OptIn(ExperimentalLayoutApi::class)
private fun FlowItems(items: List<String>, color: Color = Color.Yellow) = FlowRow(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 4.dp),
) {
    items.forEach {
        Button(
            modifier = Modifier.padding(
                horizontal = 4.dp,
                vertical = 2.dp
            ),
            colors = buttonColors(containerColor = color),
            onClick = { Log.info(it) } // TODO do something here
        ) {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun toItems(it: MarvelCollection) = it.items.map { it.name }

private sealed interface Entry {

    val type: Int

    class CrossHooks(val items: List<String>, override val type: Int = TYPE_CROSS_HOOK) : Entry
    class Image(val url: String, override val type: Int = TYPE_IMAGE) : Entry
    class Section(val title: String, override val type: Int = TYPE_SECTION) : Entry
    class WebHooks(val urls: List<MarvelUrl>, override val type: Int = TYPE_WEB_HOOK) : Entry
}

private const val TYPE_SECTION = 1
private const val TYPE_IMAGE = TYPE_SECTION + 1
private const val TYPE_WEB_HOOK = TYPE_IMAGE + 1
private const val TYPE_CROSS_HOOK = TYPE_WEB_HOOK + 1
