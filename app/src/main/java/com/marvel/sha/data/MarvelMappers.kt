package com.marvel.sha.data

import bx.extension.fromJson
import com.google.gson.Gson
import com.marvel.sha.data.characters.CharactersEntity
import com.marvel.sha.domain.MarvelCharacter
import com.marvel.sha.domain.MarvelComic
import commarvel.sha.datacomics.Comic

internal fun Map<String, Any>.int(key: String) = (get(key) as Double).toInt() // gson :D
internal fun Map<String, Any>.string(key: String) = get(key) as String

internal fun Gson.toRoom(idx: Int, it: Map<String, Any>) = CharactersEntity(
    idx = idx,
    id = it.int("id"),
    data = toJson(it),
)

internal fun Gson.fromRoom(it: CharactersEntity) = fromJson<MarvelCharacter>(it.data).apply {
    assert(it.id == id) {
        """
            id mismatch:
            room: $it
            domain: $this
            """.trimIndent()
    }
}

internal fun Gson.fromSqlDelight(it: Comic) = fromJson<MarvelComic>(it.json).apply {
    assert(it.id == id.toLong()) {
        """
            id mismatch:
            room: $it
            domain: $this
            """.trimIndent()
    }
}
