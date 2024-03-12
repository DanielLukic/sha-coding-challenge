package com.marvel.sha.ui.creators

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.marvel.sha.ui.Creators

internal class CreatorListViewModel(
    private val creators: Creators,
) : ViewModel() {
    fun observe() = creators.observe().cachedIn(viewModelScope)
}
