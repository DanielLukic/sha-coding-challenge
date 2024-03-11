package com.marvel.sha.ui

import com.marvel.sha.domain.MarvelEntity
import kotlinx.coroutines.flow.Flow

internal interface CharacterDetail {
    fun retrieve(characterId: String): Flow<MarvelEntity>
}
