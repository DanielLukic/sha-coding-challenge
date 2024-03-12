package com.marvel.sha.data.creators

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface CreatorDao {

    @Query("SELECT COUNT(id) FROM creator")
    fun count(): Int

    @Query("SELECT * FROM creator ORDER BY idx LIMIT :limit OFFSET :from")
    fun paged(from: Int, limit: Int): List<CreatorEntity>

    @Query("SELECT * FROM creator ORDER BY idx ASC")
    fun paging(): PagingSource<Int, CreatorEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(entities: List<CreatorEntity>)

    @Query("SELECT * FROM creator WHERE id = :id")
    fun byId(id: String): CreatorEntity

    @Query("DELETE FROM creator")
    suspend fun clear()

}
