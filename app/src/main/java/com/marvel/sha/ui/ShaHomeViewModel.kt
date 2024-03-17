package com.marvel.sha.ui

import androidx.lifecycle.ViewModel
import com.marvel.sha.domain.MarvelAttribution

internal class ShaHomeViewModel(
    private val attribution: MarvelAttribution
) : ViewModel() {
    val attributionFlow get() = attribution.flow
}
