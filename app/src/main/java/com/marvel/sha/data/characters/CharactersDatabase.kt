package com.marvel.sha.data.characters

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CharactersEntity::class], version = 5)
internal abstract class CharactersDatabase : RoomDatabase() {
    abstract fun characters(): CharactersDao
}
