@file:Suppress("UNCHECKED_CAST")

package com.marvel.sha.data

import bx.extension.fromJson
import com.google.gson.Gson
import com.marvel.sha.domain.MarvelEntity
import com.marvel.sha.domain.MarvelThumbnail

internal fun Map<String, Any>.int(key: String) = (get(key) as Double).toInt() // gson :D
internal fun Map<String, Any>.map(key: String) = get(key) as Map<String, Any>
internal fun Map<String, Any>.string(key: String) = get(key) as String

internal fun toMarvelEntity(it: Map<String, Any>) = MarvelEntity(
    id = it.int("id"),
    name = it.string("name"),
    description = it.string("description"),
    resourceURI = it.string("resourceURI"),
    thumbnail = toMarvelThumbnail(it.map("thumbnail")),
)

internal fun toMarvelThumbnail(it: Map<String, Any>) = MarvelThumbnail(
    path = it.string("path"),
    extension = it.string("extension"),
)

internal fun Gson.toRoomMarvelEntity(idx: Int, it: Map<String, Any>) = RoomMarvelEntity(
    idx = idx,
    id = it.int("id"),
    data = toJson(it),
)

internal fun Gson.toMarvelEntity(it: RoomMarvelEntity) = fromJson<MarvelEntity>(it.data).apply {
    assert(it.id == id) {
        """
            id mismatch:
            room: $it
            domain: $this
            """.trimIndent()
    }
}
