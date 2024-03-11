package com.marvel.sha.ui.home

import androidx.annotation.StringRes
import com.marvel.sha.R

internal enum class HomePage(@StringRes val title: Int) {
    CHARACTERS(R.string.characters),
    COMICS(R.string.comics),
    CREATORS(R.string.creators),
    SERIES(R.string.series),
    STORIES(R.string.stories),
}
