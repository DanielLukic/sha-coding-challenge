package com.marvel.sha.data.characters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import bx.logging.Log
import bx.system.Cx
import com.google.gson.Gson
import com.marvel.sha.data.MarvelData
import com.marvel.sha.data.MarvelResponse
import com.marvel.sha.data.fromRoom
import com.marvel.sha.data.toRoom
import com.marvel.sha.domain.MarvelCharacter
import java.io.IOException
import kotlinx.coroutines.withContext
import retrofit2.HttpException

internal class CharactersPagingSource(
    private val dao: CharactersDao,
    private val gson: Gson,
    private val load: suspend (Int) -> MarvelResponse,
) : PagingSource<Int, MarvelCharacter>() {

    private fun MarvelData.isLastPage() = count == 0

    override fun getRefreshKey(state: PagingState<Int, MarvelCharacter>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarvelCharacter> = try {
        val offset = params.key ?: 0
        val (data, entities) = withContext(Cx.IO) { page(offset) }
        Log.info { data.copy(results = emptyList()) }
        val prev = (offset - data.limit).coerceAtLeast(0)
        val next = (offset + data.count)
        LoadResult.Page(
            data = entities,
            prevKey = if (offset == 0) null else prev,
            nextKey = if (data.isLastPage()) null else next,
        )
    }
    catch (exception: IOException) {
        LoadResult.Error(exception)
    }
    catch (exception: HttpException) {
        LoadResult.Error(exception)
    }

    private suspend fun page(offset: Int): Pair<MarvelData, List<MarvelCharacter>> {

        var envelope: MarvelData? = null

        val cachedCount = dao.count()
        Log.info("cached count from room: $cachedCount")
        if (cachedCount < offset + PAGE_SIZE) {

            Log.info("network load at offset: $offset")

            val response = load(offset)
            assert(response.code == 200) { response }
            envelope = response.data

            Log.info("network response: ${response.data.copy(results = emptyList())}")

            val entities = response.data.results.mapIndexed { idx, it ->
                gson.toRoom(offset + idx, it)
            }

            Log.info("store ${entities.size} entities into room")
            dao.upsert(entities)

        }

        Log.info("slurp from room $offset .. ${offset.plus(PAGE_SIZE)}")
        val entities = dao.paged(offset, offset + PAGE_SIZE - 1).map { gson.fromRoom(it) }
        Log.warn { "slurped ${entities.size} entities" }
        envelope = envelope ?: MarvelData(offset, PAGE_SIZE, 0, entities.size, emptyList())

        Log.info("resulting envelope ${envelope.copy(results = emptyList())}")

        return envelope to entities

    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
