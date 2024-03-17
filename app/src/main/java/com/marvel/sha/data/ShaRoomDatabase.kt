package com.marvel.sha.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marvel.sha.data.characters.CharacterDao
import com.marvel.sha.data.characters.CharacterEntity
import com.marvel.sha.data.comics.ComicDao
import com.marvel.sha.data.comics.ComicEntity
import com.marvel.sha.data.creators.CreatorDao
import com.marvel.sha.data.creators.CreatorEntity

@Database(entities = [CharacterEntity::class, ComicEntity::class, CreatorEntity::class], version = 13)
internal abstract class ShaRoomDatabase : RoomDatabase() {
    abstract fun characters(): CharacterDao
    abstract fun comics(): ComicDao
    abstract fun creators(): CreatorDao
}
