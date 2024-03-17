package com.marvel.sha.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import bx.logging.Log
import bx.util.SuperIoContext
import bx.util.tryCatching
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
internal open class MarvelRemoteMediator<T : Any>(
    private val dao: MarvelDao<T>,
    private val service: MarvelService,
    private val query: String,
    private val filterName: String,
    private val path: String,
    private val orderBy: String,
    private val toRoom: (idx: Int, data: Map<String, Any>) -> T,
) : RemoteMediator<Int, T>() {

    init {
        Log.warn { "$this $query" }
    }

    private val context = SuperIoContext(this.toString())

    private var once = false

    override suspend fun initialize() = if (once) {
        InitializeAction.SKIP_INITIAL_REFRESH
    }
    else {
        once = true
        InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, T>
    ): MediatorResult = if (loadType == LoadType.PREPEND) {
        MediatorResult.Success(endOfPaginationReached = true)
    }
    else withContext(context) {

        val result = tryCatching {

            if (loadType == LoadType.REFRESH) dao.clear()

            val offset = dao.maxIdx()?.let { if (it == 0) 0 else it + 1 } ?: 0
            val response = service.pagedAt(path, orderBy, offset, query, filterName)
            assert(response.code == 200) { response }

            Log.info("network response: ${response.data.copy(results = emptyList())}")
            val entities = response.data.results.mapIndexed { idx, it -> toRoom(offset + idx, it) }
            dao.upsert(entities)

            MediatorResult.Success(response.data.isLastPage())
        }

        result.onFailure { Log.error(it) }

        result.valueOrNull() ?: MediatorResult.Error(result.failureOrNull()!!)
    }

}
