package com.marvel.sha.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import bx.logging.Log
import bx.util.tryCatching
import com.marvel.sha.domain.MarvelEntity
import com.marvel.sha.ui.CharacterList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class CharacterListViewModel(
    private val characters: CharacterList,
) : ViewModel() {

    private val state = MutableStateFlow<PagingData<MarvelEntity>>(PagingData.empty())

    init {
        viewModelScope.launch {
            tryCatching {
                characters.observe().cachedIn(viewModelScope).collect { state.value = it }
            }.onFailure {
                Log.error(it, "ignored for this demo - but ideally show error and allow retry")
            }
        }
    }

    fun observe(): StateFlow<PagingData<MarvelEntity>> = state.asStateFlow()

}
