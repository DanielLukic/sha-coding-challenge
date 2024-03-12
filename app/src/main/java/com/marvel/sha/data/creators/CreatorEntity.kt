package com.marvel.sha.data.creators

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("creator")
internal data class CreatorEntity(
    @PrimaryKey val idx: Int, // remote index for paging
    val id: Int,
    val fullName: String,
    val data: String,
)
