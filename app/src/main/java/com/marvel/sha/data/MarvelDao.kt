package com.marvel.sha.data

internal interface MarvelDao<T> {
    suspend fun clear()
    suspend fun maxIdx(): Int?
    suspend fun upsert(entities: List<T>)
}
