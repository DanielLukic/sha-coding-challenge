package com.marvel.sha.data

import androidx.paging.*
import com.google.gson.Gson
import com.marvel.sha.data.characters.CharacterEntity
import com.marvel.sha.data.comics.ComicEntity
import com.marvel.sha.data.creators.CreatorEntity
import com.marvel.sha.domain.MarvelCharacter
import com.marvel.sha.domain.MarvelComic
import com.marvel.sha.domain.MarvelCreator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
@Suppress("MemberVisibilityCanBePrivate")
internal class MarvelRepository(
    private val database: ShaRoomDatabase,
    private val service: MarvelService,
    private val gson: Gson,
) {
    private val characters by lazy { database.characters() }
    private val comics by lazy { database.comics() }
    private val creators by lazy { database.creators() }

    suspend fun characterDetail(id: String) = characters.byId(id).let(gson::fromRoom)
    suspend fun comicDetail(id: String) = comics.byId(id).let(gson::fromRoom)
    suspend fun creatorDetail(id: String) = creators.byId(id).let(gson::fromRoom)

    fun characterList(query: String): Flow<PagingData<MarvelCharacter>> = Pager<Int, CharacterEntity>(
        config = PagingConfig(pageSize = 20, prefetchDistance = 0),
        remoteMediator = MarvelRemoteMediator(
            dao = characterDao(),
            service = service,
            query = query,
            filterName = "nameStartsWith",
            path = "characters",
            orderBy = "name",
            toRoom = gson::toCharacterEntity,
        ),
        pagingSourceFactory = { characters.paging() },
    ).flow.map { data -> data.map(gson::fromRoom) }

    fun comicList(query: String): Flow<PagingData<MarvelComic>> = Pager<Int, ComicEntity>(
        config = PagingConfig(pageSize = 20, prefetchDistance = 0),
        remoteMediator = MarvelRemoteMediator(
            dao = comicDao(),
            service = service,
            query = query,
            filterName = "titleStartsWith",
            path = "comics",
            orderBy = "title",
            toRoom = gson::toComicEntity,
        ),
        pagingSourceFactory = { comics.paging() },
    ).flow.map { data -> data.map(gson::fromRoom) }

    fun creatorList(query: String): Flow<PagingData<MarvelCreator>> = Pager<Int, CreatorEntity>(
        config = PagingConfig(pageSize = 20, prefetchDistance = 0),
        remoteMediator = MarvelRemoteMediator(
            dao = creatorDao(),
            service = service,
            query = query,
            filterName = "nameStartsWith",
            path = "creators",
            orderBy = "firstName,lastName",
            toRoom = gson::toCreatorEntity,
        ),
        pagingSourceFactory = { creators.paging() },
    ).flow.map { data -> data.map(gson::fromRoom) }

    private fun characterDao() = object : MarvelDao<CharacterEntity> {
        override suspend fun clear() = characters.clear()
        override suspend fun maxIdx() = characters.maxIdx()
        override suspend fun upsert(entities: List<CharacterEntity>) = characters.upsert(entities)
    }

    private fun comicDao() = object : MarvelDao<ComicEntity> {
        override suspend fun clear() = comics.clear()
        override suspend fun maxIdx() = comics.maxIdx()
        override suspend fun upsert(entities: List<ComicEntity>) = comics.upsert(entities)
    }

    private fun creatorDao() = object : MarvelDao<CreatorEntity> {
        override suspend fun clear() = creators.clear()
        override suspend fun maxIdx() = creators.maxIdx()
        override suspend fun upsert(entities: List<CreatorEntity>) = creators.upsert(entities)
    }
}
