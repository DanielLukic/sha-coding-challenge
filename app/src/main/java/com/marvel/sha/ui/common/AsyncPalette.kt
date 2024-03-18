package com.marvel.sha.ui.common

import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import bx.logging.Log
import bx.util.Optional
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.suspendCancellableCoroutine

@Composable
internal fun AsyncPalette(
    source: String,
    palette: MutableState<Optional<Palette>> = remember { mutableStateOf(Optional.empty()) },
) {
    val context = LocalContext.current
    val loader = remember { ImageLoader(context).newBuilder().allowHardware(false).build() }
    LaunchedEffect(source) {
        val request = ImageRequest.Builder(context).data(source).build()
        val bd = loader.execute(request).drawable as? BitmapDrawable ?: return@LaunchedEffect
        suspendCancellableCoroutine<Unit> {
            val bitmap = bd.bitmap ?: return@suspendCancellableCoroutine
            palette.value = Optional.of(Palette.from(bitmap).generate())
            it.invokeOnCancellation { bitmap.recycle().also { Log.info("recycled") } }
        }
    }
}
