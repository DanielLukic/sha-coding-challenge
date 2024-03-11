package com.marvel.sha.data.characters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CharactersDao {

    @Query("SELECT COUNT(idx) FROM charactersentity")
    fun count(): Int

    @Query("SELECT * FROM charactersentity WHERE idx BETWEEN :from AND :toInclusive ORDER BY idx ASC")
    fun paged(from: Int, toInclusive: Int): List<CharactersEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(entities: List<CharactersEntity>)

    @Query("SELECT * FROM charactersentity WHERE id = :id")
    fun byId(id: String): Flow<CharactersEntity>

    @Query("DELETE FROM charactersentity")
    suspend fun clear()

}
