package com.marvel.sha.data.characters

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class CharactersEntity(
    @PrimaryKey val idx: Int, // hack 1: use result index instead of id
    val id: Int, // for direct lookup in detail screen
    val data: String, // hack 2: json of the entity data instead of gazillion fields we never query
)
