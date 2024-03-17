package com.marvel.sha.ui

import androidx.annotation.StringRes
import com.marvel.sha.R

internal enum class ShaPage(@StringRes val title: Int) {
    COMICS(R.string.comics),
    CHARACTERS(R.string.characters),
    CREATORS(R.string.creators),
}
