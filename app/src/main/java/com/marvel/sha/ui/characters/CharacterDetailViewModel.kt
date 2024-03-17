package com.marvel.sha.ui.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bx.util.Optional
import com.marvel.sha.domain.MarvelCharacter
import com.marvel.sha.ui.MarvelDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val domain: MarvelDomain,
) : ViewModel() {

    private val id = savedStateHandle.get<String>("characterId")

    private val state = MutableStateFlow<Optional<MarvelCharacter>>(Optional.empty())

    init {
        viewModelScope.launch {
            domain.characterDetail(id ?: error("null  id")).let { state.value = Optional.of(it) }
        }
    }

    fun observe() = state.asStateFlow()

}
