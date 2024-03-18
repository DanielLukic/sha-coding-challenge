package com.marvel.sha.ui.comics

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

internal fun Color.lighten() = android.graphics.Color.HSVToColor(FloatArray(3).apply {
    android.graphics.Color.colorToHSV(this@lighten.toArgb(), this)
    this[2] *= 1.2f
}).let(::Color)
