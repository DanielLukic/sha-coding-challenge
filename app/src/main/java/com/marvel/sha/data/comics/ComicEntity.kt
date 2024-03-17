package com.marvel.sha.data.comics

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("comic")
internal data class ComicEntity(
    val idx: Int,
    @PrimaryKey val id: Int,
    val data: String,
)
