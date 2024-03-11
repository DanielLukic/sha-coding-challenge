package com.marvel.sha.data

import bx.extension.fromJson
import com.google.gson.Gson
import com.marvel.sha.domain.MarvelEntity

internal fun Map<String, Any>.int(key: String) = (get(key) as Double).toInt() // gson :D
internal fun Map<String, Any>.string(key: String) = get(key) as String

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
