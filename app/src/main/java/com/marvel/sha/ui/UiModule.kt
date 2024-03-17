package com.marvel.sha.ui

import com.marvel.sha.ui.characters.CharacterDetailViewModel
import com.marvel.sha.ui.characters.CharacterListViewModel
import com.marvel.sha.ui.comics.ComicDetailViewModel
import com.marvel.sha.ui.comics.ComicListViewModel
import com.marvel.sha.ui.creators.CreatorDetailViewModel
import com.marvel.sha.ui.creators.CreatorListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal object UiModule {
    operator fun invoke() = module {
        viewModelOf(::CharacterDetailViewModel)
        viewModelOf(::CharacterListViewModel)
        viewModelOf(::ComicDetailViewModel)
        viewModelOf(::ComicListViewModel)
        viewModelOf(::CreatorListViewModel)
        viewModelOf(::CreatorDetailViewModel)
        viewModelOf(::ShaHomeViewModel)
    }
}
