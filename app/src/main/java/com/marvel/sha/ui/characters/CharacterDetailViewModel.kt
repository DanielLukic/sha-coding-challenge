package com.marvel.sha.ui.characters

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bx.util.Optional
import com.marvel.sha.domain.MarvelEntity
import com.marvel.sha.ui.CharacterDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val characterDetail: CharacterDetail,
) : ViewModel() {

    private val characterId = savedStateHandle.get<String>("characterId")

    private val state = MutableStateFlow<Optional<MarvelEntity>>(Optional.empty())

    init {
        viewModelScope.launch {
            characterDetail.retrieve(characterId ?: error("null character id")).collect {
                state.value = Optional.of(it)
            }
        }
    }

    var color = MutableStateFlow(Color.Yellow)

    fun observe() = state.asStateFlow()

}
