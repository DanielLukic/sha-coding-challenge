package com.marvel.sha.data.characters

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface CharacterDao {

    @Query("SELECT COUNT(id) FROM character")
    suspend fun count(): Int

    @Query("SELECT MAX(idx) FROM character")
    suspend fun maxIdx(): Int?

    @Query("SELECT * FROM character ORDER BY idx ASC")
    fun paging(): PagingSource<Int, CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entities: List<CharacterEntity>)

    @Query("SELECT * FROM character WHERE id = :id")
    suspend fun byId(id: String): CharacterEntity

    @Query("DELETE FROM character")
    suspend fun clear()

}
