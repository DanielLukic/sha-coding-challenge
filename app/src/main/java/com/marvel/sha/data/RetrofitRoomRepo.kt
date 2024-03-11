package com.marvel.sha.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import bx.system.Clock
import com.google.gson.Gson
import com.marvel.sha.BuildConfig
import com.marvel.sha.domain.MarvelEntity
import java.security.MessageDigest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

internal class RetrofitRoomRepo(
    private val database: RoomShaDatabase,
    private val service: RetrofitService,
    private val clock: Clock,
    private val gson: Gson,
) {
    private val dao by lazy { database.characters() }

    val attribution = MutableStateFlow("")

    // in theory get more details from backend. but afaict there aren't any for this api...
    fun characterDetail(characterId: String) = dao.byId(characterId).map { gson.toMarvelEntity(it) }

    fun characters(): Flow<PagingData<MarvelEntity>> = Pager(
        config = PagingConfig(
            pageSize = RetrofitRoomPagingSource.PAGE_SIZE,
            prefetchDistance = 2,
        ),
        pagingSourceFactory = { createPagingSource() },
    ).flow

    private fun createPagingSource() = RetrofitRoomPagingSource(dao, gson) { offset ->
        service.characters(offset).also {
            assert(it.code == 200) { it }
            attribution.value = it.attributionText
        }
    }

    private suspend fun RetrofitService.characters(offset: Int) = with(auth()) {
        characters(ts, apikey, hash, offset)
    }

    private fun auth(): Auth {
        val ts = clock.instant().minusMillis(1710091030000).toEpochMilli().toString()
        val private = PRIVATE_API_KEY
        val public = PUBLIC_API_KEY
        val input = ts + private + public
        val hash = md5(input)
        return Auth(ts, public, hash)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun md5(input: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val output = digest.digest(input.toByteArray())
        return output.toHexString()
    }

    private data class Auth(
        val ts: String,
        val apikey: String,
        val hash: String,
    )

    private companion object {
        const val PUBLIC_API_KEY = BuildConfig.PUBLIC_API_KEY
        const val PRIVATE_API_KEY = BuildConfig.PRIVATE_API_KEY
    }

}
