package com.marvel.sha.data.creators

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface CreatorDao {

    @Query("SELECT COUNT(id) FROM creator")
    suspend fun count(): Int

    @Query("SELECT MAX(idx) FROM creator")
    suspend fun maxIdx(): Int

    @Query("SELECT * FROM creator ORDER BY idx ASC")
    fun paging(): PagingSource<Int, CreatorEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entities: List<CreatorEntity>)

    @Query("SELECT * FROM creator WHERE id = :id")
    suspend fun byId(id: String): CreatorEntity

    @Query("DELETE FROM creator")
    suspend fun clear()

}
