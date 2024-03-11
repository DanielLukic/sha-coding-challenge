package com.marvel.sha.data.characters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marvel.sha.data.RoomMarvelEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface RoomCharactersDao {

    @Query("SELECT COUNT(idx) FROM roommarvelentity")
    fun count(): Int

    @Query("SELECT * FROM roommarvelentity WHERE idx BETWEEN :from AND :toInclusive ORDER BY idx ASC")
    fun paged(from: Int, toInclusive: Int): List<RoomMarvelEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(entities: List<RoomMarvelEntity>)

    @Query("SELECT * FROM roommarvelentity WHERE id = :id")
    fun byId(id: String): Flow<RoomMarvelEntity>

    @Query("DELETE FROM roommarvelentity")
    suspend fun clear()

}
