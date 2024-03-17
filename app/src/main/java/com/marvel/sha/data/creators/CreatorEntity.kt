package com.marvel.sha.data.creators

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("creator")
internal data class CreatorEntity(
    val idx: Int,
    @PrimaryKey val id: Int,
    val data: String,
)
