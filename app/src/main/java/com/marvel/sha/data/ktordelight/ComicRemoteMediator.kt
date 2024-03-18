package com.marvel.sha.data.ktordelight

import androidx.paging.*
import bx.logging.Log
import bx.system.Cx
import bx.util.AutoCancelCx
import bx.util.SafeSingleScope
import com.google.gson.Gson
import com.marvel.sha.data.MarvelResponse
import com.marvel.sha.data.int
import com.marvel.sha.data.string
import commarvel.sha.datacomics.Comic
import commarvel.sha.datacomics.ComicsQueries
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
internal class ComicRemoteMediator(
    private val database: ComicsQueries,
    private val service: ComicService,
    private val gson: Gson,
    private val query: String,
) : RemoteMediator<Int, Comic>(),
    AutoCancelCx by AutoCancelCx(SafeSingleScope("ComicRemoteMediator")) {

    lateinit var source: PagingSource<Int, Comic>

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Comic>
    ): MediatorResult = if (loadType == LoadType.PREPEND) {
        MediatorResult.Success(endOfPaginationReached = true)
    }
    else {
        val offset = if (loadType == LoadType.REFRESH) 0
        else withContext(Cx.IO) { database.maxIdx().executeAsOne().MAX?.toInt()?.plus(1) ?: 0 }

        Log.verbose { "requesting ${loadType.javaClass.simpleName} @ $offset @ $this" }

        val result = CompletableDeferred<MediatorResult>()
        requestedPages.update { it + RequestedPage(offset, result) }
        result.await()
    }

    private val requestedPages = MutableStateFlow(emptyList<RequestedPage>())

    private data class RequestedPage(
        val offset: Int,
        val result: CompletableDeferred<MediatorResult>,
    )

    // TODO revert to simple version - this was only for experimenting with "actor style" processing

    init {
        launch {
            requestedPages.collect { requests ->

                val request = requests.firstOrNull() ?: return@collect

                Log.verbose { "processing ${request.offset} @ $query @ ${this@ComicRemoteMediator}" }

                val offset = request.offset

                val result = try {
                    val response = process(offset)
                    MediatorResult.Success(response.data.isLastPage())
                }
                catch (it: Throwable) {
                    if (it is CancellationException) throw it
                    MediatorResult.Error(it)
                }

                requestedPages.update {

                    val matches = it.filter { page -> page.offset == offset }
                    assert(matches.isNotEmpty()) { matches }
                    Log.verbose { "matches ${matches.size}" }

                    val previous = matches.dropLast(1)
                    previous.forEach { page ->
                        Log.warn { "cancel $page" }
                        page.result.cancel()
                    }

                    val latest = matches.last()
                    latest.result.complete(result)

                    it - matches.toSet()
                }
            }
        }
    }

    private suspend fun process(offset: Int): MarvelResponse {

        source.invalidate()

        val response = service.comics(offset, query)
        assert(response.code == 200) { response }

        Log.info("network response: ${response.data.copy(results = emptyList())}")
        val entities = response.data.results

        database.transaction {

            if (offset == 0) database.clear()

            entities.forEachIndexed { idx, it ->
                Log.verbose("#${offset + idx}: ${it.string("title")}")
                database.insert(
                    idx = (offset + idx).toLong(),
                    id = it.int("id").toLong(),
                    title = it.string("title"),
                    json = gson.toJson(it),
                )
            }
        }

        return response
    }
}
