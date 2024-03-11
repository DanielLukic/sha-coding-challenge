package com.marvel.sha.ui.comics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import bx.logging.Log
import bx.util.tryCatching
import com.marvel.sha.domain.MarvelComic
import com.marvel.sha.ui.ComicsList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ComicsListViewModel(
    private val comics: ComicsList,
) : ViewModel() {

    private val state = MutableStateFlow<PagingData<MarvelComic>>(PagingData.empty())

    init {
        viewModelScope.launch {
            tryCatching {
                comics.observe().cachedIn(viewModelScope).collect { state.value = it }
            }.onFailure {
                Log.error(it, "ignored for this demo - but ideally show error and allow retry")
            }
        }
    }

    fun observe(): StateFlow<PagingData<MarvelComic>> = state.asStateFlow()

}