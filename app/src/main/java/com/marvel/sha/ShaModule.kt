package com.marvel.sha

import bx.system.Clock
import com.google.gson.GsonBuilder
import com.marvel.sha.data.DataModule
import com.marvel.sha.data.characters.CharacterRepository
import com.marvel.sha.data.comics.ComicsRepository
import com.marvel.sha.data.creators.CreatorRepository
import com.marvel.sha.ui.CharacterDetail
import com.marvel.sha.ui.CharactersList
import com.marvel.sha.ui.ComicDetail
import com.marvel.sha.ui.ComicsList
import com.marvel.sha.ui.Creators
import com.marvel.sha.ui.UiModule
import org.koin.dsl.module

internal object AppModule {

    operator fun invoke() = module {

        includes(DataModule(), UiModule())

        single { Clock() }
        single { GsonBuilder().setPrettyPrinting().setLenient().create() }

        // role bindings for crossing module boundaries in a decoupled way:
        single<CharacterDetail> { BindCharacterDetail(get()) }
        single<CharactersList> { BindCharactersList(get()) }
        single<ComicDetail> { BindComicDetail(get()) }
        single<ComicsList> { BindComicsList(get()) }
        single<Creators> { BindCreators(get()) }

    }

    private class BindCharacterDetail(private val repo: CharacterRepository) : CharacterDetail {
        override fun retrieve(characterId: String) = repo.characterDetail(characterId)
    }

    private class BindCharactersList(private val repo: CharacterRepository) : CharactersList {
        override fun observe() = repo.characterList()
    }

    private class BindComicDetail(private val repo: ComicsRepository) : ComicDetail {
        override fun retrieve(comicId: String) = repo.comicDetail(comicId)
    }

    private class BindComicsList(private val repo: ComicsRepository) : ComicsList {
        override fun observe() = repo.comics()
    }

    private class BindCreators(private val repo: CreatorRepository) : Creators {
        override fun observe() = repo.creatorList()
        override fun retrieve(id: String) = TODO()
    }

}
