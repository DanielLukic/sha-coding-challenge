package com.marvel.sha.data.characters

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("character")
internal data class CharacterEntity(
    val idx: Int,
    @PrimaryKey val id: Int,
    val data: String,
)
