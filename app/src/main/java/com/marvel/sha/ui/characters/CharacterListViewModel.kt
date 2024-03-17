package com.marvel.sha.ui.characters

import com.marvel.sha.domain.MarvelCharacter
import com.marvel.sha.ui.MarvelDomain
import com.marvel.sha.ui.searchlist.SearchListViewModel

internal class CharacterListViewModel(private val domain: MarvelDomain) :
    SearchListViewModel<MarvelCharacter>(data = domain::characterList)
