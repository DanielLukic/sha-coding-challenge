package com.marvel.sha.ui.creators

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bx.util.Optional
import com.marvel.sha.domain.MarvelCreator
import com.marvel.sha.ui.MarvelDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class CreatorDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val domain: MarvelDomain,
) : ViewModel() {

    private val id = savedStateHandle.get<String>("creatorId")

    private val state = MutableStateFlow<Optional<MarvelCreator>>(Optional.empty())

    init {
        viewModelScope.launch {
            domain.creatorDetail(id ?: error("null id")).let { state.value = Optional.of(it) }
        }
    }

    fun observe() = state.asStateFlow()

}
