package com.marvel.sha.ui.creators

import com.marvel.sha.domain.MarvelCreator
import com.marvel.sha.ui.MarvelDomain
import com.marvel.sha.ui.searchlist.SearchListViewModel

internal class CreatorListViewModel(private val domain: MarvelDomain) :
    SearchListViewModel<MarvelCreator>(data = domain::creatorList)
