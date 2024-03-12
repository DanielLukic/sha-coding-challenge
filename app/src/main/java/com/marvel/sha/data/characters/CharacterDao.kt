package com.marvel.sha.data.characters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CharacterDao {

    @Query("SELECT COUNT(idx) FROM character")
    fun count(): Int

    @Query("SELECT * FROM character WHERE idx BETWEEN :from AND :toInclusive ORDER BY idx ASC")
    fun paged(from: Int, toInclusive: Int): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(entities: List<CharacterEntity>)

    @Query("SELECT * FROM character WHERE id = :id")
    fun byId(id: String): Flow<CharacterEntity>

    @Query("DELETE FROM character")
    suspend fun clear()

}
