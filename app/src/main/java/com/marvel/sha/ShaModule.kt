package com.marvel.sha

import bx.system.Clock
import com.google.gson.GsonBuilder
import com.marvel.sha.data.DataModule
import com.marvel.sha.data.characters.CharactersRepository
import com.marvel.sha.ui.CharacterDetail
import com.marvel.sha.ui.CharacterList
import com.marvel.sha.ui.UiModule
import org.koin.dsl.module

internal object AppModule {

    operator fun invoke() = module {

        includes(DataModule(), UiModule())

        single { Clock() }
        single { GsonBuilder().setPrettyPrinting().setLenient().create() }

        // role bindings for crossing module boundaries in a decoupled way:
        single<CharacterDetail> { BindCharacterDetail(get()) }
        single<CharacterList> { BindCharacterList(get()) }

    }

    private class BindCharacterDetail(private val repo: CharactersRepository) : CharacterDetail {
        override fun retrieve(characterId: String) = repo.characterDetail(characterId)
    }

    private class BindCharacterList(private val repo: CharactersRepository) : CharacterList {
        override fun observe() = repo.characters()
    }

}
