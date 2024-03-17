package com.marvel.sha.ui.comics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bx.util.Optional
import com.marvel.sha.domain.MarvelComic
import com.marvel.sha.ui.ComicDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ComicDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val comicDetail: ComicDetail,
) : ViewModel() {

    private val comicId = savedStateHandle.get<String>("comicId")

    private val state = MutableStateFlow<Optional<MarvelComic>>(Optional.empty())

    init {
        viewModelScope.launch {
            comicDetail.retrieve(comicId ?: error("null comic id")).let {
                state.value = Optional.of(it)
            }
        }
    }

    fun observe() = state.asStateFlow()

}
