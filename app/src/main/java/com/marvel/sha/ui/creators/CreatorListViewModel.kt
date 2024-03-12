package com.marvel.sha.ui.creators

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import bx.logging.Log
import bx.util.tryCatching
import com.marvel.sha.domain.MarvelCreator
import com.marvel.sha.ui.Creators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class CreatorListViewModel(
    private val creators: Creators,
) : ViewModel() {

    private val state = MutableStateFlow<PagingData<MarvelCreator>>(PagingData.empty())

    init {
        viewModelScope.launch {
            tryCatching {
                creators.observe().cachedIn(viewModelScope).collect { state.value = it }
            }.onFailure {
                Log.error(it, "ignored for this demo - but ideally show error and allow retry")
            }
        }
    }

    fun observe(): StateFlow<PagingData<MarvelCreator>> = state.asStateFlow()

}
