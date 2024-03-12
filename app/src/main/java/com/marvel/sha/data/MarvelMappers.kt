package com.marvel.sha.data

import bx.extension.fromJson
import com.google.gson.Gson
import com.marvel.sha.data.characters.CharacterEntity
import com.marvel.sha.data.creators.CreatorEntity
import com.marvel.sha.domain.MarvelCharacter
import com.marvel.sha.domain.MarvelComic
import com.marvel.sha.domain.MarvelCreator
import commarvel.sha.datacomics.Comic

internal fun Map<String, Any>.int(key: String) = (get(key) as Double).toInt() // gson :D
internal fun Map<String, Any>.string(key: String) = get(key) as String

internal fun Gson.toCharacterEntity(idx: Int, it: Map<String, Any>) = CharacterEntity(
    idx = idx,
    id = it.int("id"),
    data = toJson(it),
)

internal fun Gson.toCreatorEntity(it: Map<String, Any>) = CreatorEntity(
    id = it.int("id"),
    fullName = it.string("fullName"),
    data = toJson(it),
)

internal fun Gson.fromRoom(it: CharacterEntity) = fromJson<MarvelCharacter>(it.data).apply {
    assert(it.id == id) {
        """
            id mismatch:
            room: $it
            domain: $this
            """.trimIndent()
    }
}

internal fun Gson.fromRoom(it: CreatorEntity) = fromJson<MarvelCreator>(it.data).apply {
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
