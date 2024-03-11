package com.marvel.sha.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marvel.sha.data.characters.RoomCharactersDao

@Database(entities = [RoomMarvelEntity::class], version = 4)
internal abstract class RoomShaDatabase : RoomDatabase() {
    abstract fun characters(): RoomCharactersDao
}
