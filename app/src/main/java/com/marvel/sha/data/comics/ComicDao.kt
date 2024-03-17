package com.marvel.sha.data.comics

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface ComicDao {

    @Query("SELECT COUNT(id) FROM comic")
    suspend fun count(): Int

    @Query("SELECT MAX(idx) FROM comic")
    suspend fun maxIdx(): Int

    @Query("SELECT * FROM comic ORDER BY idx ASC")
    fun paging(): PagingSource<Int, ComicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entities: List<ComicEntity>)

    @Query("SELECT * FROM comic WHERE id = :id")
    suspend fun byId(id: String): ComicEntity

    @Query("DELETE FROM comic")
    suspend fun clear()

}
