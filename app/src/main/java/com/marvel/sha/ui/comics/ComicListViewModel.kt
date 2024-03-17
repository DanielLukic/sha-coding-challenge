package com.marvel.sha.ui.comics

import com.marvel.sha.domain.MarvelComic
import com.marvel.sha.ui.ComicList
import com.marvel.sha.ui.searchlist.SearchListViewModel

internal class ComicListViewModel(private val domain: ComicList) :
    SearchListViewModel<MarvelComic>(data = domain::observe)
