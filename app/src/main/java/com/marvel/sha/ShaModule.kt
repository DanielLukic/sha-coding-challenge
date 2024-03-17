package com.marvel.sha

import bx.system.Clock
import com.google.gson.GsonBuilder
import com.marvel.sha.data.DataModule
import com.marvel.sha.data.MarvelRepository
import com.marvel.sha.data.ktordelight.ComicRepository
import com.marvel.sha.ui.ComicDetail
import com.marvel.sha.ui.ComicList
import com.marvel.sha.ui.MarvelDomain
import com.marvel.sha.ui.UiModule
import org.koin.dsl.module

internal object ShaModule {

    operator fun invoke() = module {

        includes(DataModule(), UiModule())

        single { Clock() }
        single { GsonBuilder().setPrettyPrinting().setLenient().create() }

        // role bindings for crossing module boundaries in a decoupled way:
        single<ComicDetail> { BindComicDetail(get()) }
        single<ComicList> { BindComicList(get()) }
        single<MarvelDomain> { BindMarvelDomain(get()) }

    }

    private class BindComicDetail(private val repo: ComicRepository) : ComicDetail {
        override suspend fun retrieve(comicId: String) = repo.comicDetail(comicId)
    }

    private class BindComicList(private val repo: ComicRepository) : ComicList {
        override fun observe(query: String) = repo.comics(query)
    }

    private class BindMarvelDomain(private val repo: MarvelRepository) : MarvelDomain {
        override fun characterList(query: String) = repo.characterList(query)
        override fun comicList(query: String) = repo.comicList(query)
        override fun creatorList(query: String) = repo.creatorList(query)
        override suspend fun characterDetail(id: String) = repo.characterDetail(id)
        override suspend fun comicDetail(id: String) = repo.comicDetail(id)
        override suspend fun creatorDetail(id: String) = repo.creatorDetail(id)
    }

}
