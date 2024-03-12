package com.marvel.sha.data.creators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.map
import bx.logging.Log
import bx.system.Cx
import bx.util.tryCatching
import com.google.gson.Gson
import com.marvel.sha.data.MarvelData
import com.marvel.sha.data.ShaRoomDatabase
import com.marvel.sha.data.fromRoom
import com.marvel.sha.data.toCreatorEntity
import com.marvel.sha.domain.MarvelCreator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
@Suppress("MemberVisibilityCanBePrivate")
internal class CreatorRepository(
    private val database: ShaRoomDatabase,
    private val service: CreatorService,
    private val gson: Gson,
) {
    private val dao by lazy { database.creators() }

    val copyright = MutableStateFlow("")
    val attribution = MutableStateFlow("")

    // in theory get more details from backend. but afaict there aren't any for this api...
    fun creatorDetail(creatorId: String) = dao.byId(creatorId).let { gson.fromRoom(it) }

    fun creatorList(): Flow<PagingData<MarvelCreator>> = Pager(

        config = PagingConfig(
            pageSize = CreatorPagingSource.PAGE_SIZE,
            prefetchDistance = 2,
        ),

        remoteMediator = object : RemoteMediator<Int, CreatorEntity>() {

            override suspend fun initialize() = InitializeAction.SKIP_INITIAL_REFRESH

            override suspend fun load(
                loadType: LoadType,
                state: PagingState<Int, CreatorEntity>
            ): MediatorResult = if (loadType == LoadType.PREPEND) {
                MediatorResult.Success(endOfPaginationReached = true)
            }
            else withContext(Cx.IO) {

                val result = tryCatching {

                    Log.info { "load type? $loadType" }
                    Log.info { "state pos? " + state.anchorPosition }
                    Log.info { "state empty? " + state.isEmpty() }
                    if (loadType == LoadType.REFRESH) dao.clear()

                    val offset = state.lastItemOrNull()?.idx?.plus(1) ?: 0
                    val response = service.pagedAt(offset)
                    assert(response.code == 200) { response }

                    Log.info("network response: ${response.data.copy(results = emptyList())}")
                    val entities = response.data.results.mapIndexed { idx, it ->
                        gson.toCreatorEntity(offset + idx, it)
                    }

                    Log.info("store ${entities.size} entities into room")
                    dao.upsert(entities)

                    MediatorResult.Success(response.data.isLastPage())

                }

                result.valueOrNull() ?: MediatorResult.Error(result.failureOrNull()!!)

            }

        },

        pagingSourceFactory = {
            Log.info { "CREATE PAGING SOURCE" }
            dao.paging()
        }

    ).flow.map { data -> data.map { entity -> gson.fromRoom(entity) } }

    private fun MarvelData.isLastPage() = count == 0 || (offset + count) == total
}
