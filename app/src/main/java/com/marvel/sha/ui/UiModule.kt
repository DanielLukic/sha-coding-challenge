package com.marvel.sha.ui

import com.marvel.sha.ui.characters.CharacterDetailViewModel
import com.marvel.sha.ui.characters.CharactersListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal object UiModule {

    operator fun invoke() = module {
        viewModelOf(::CharacterDetailViewModel)
        viewModelOf(::CharactersListViewModel)
    }

}
