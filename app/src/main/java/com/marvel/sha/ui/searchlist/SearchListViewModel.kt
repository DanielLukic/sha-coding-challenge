package com.marvel.sha.ui.searchlist

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
internal abstract class SearchListViewModel<T : Any>(
    private val data: (String) -> Flow<PagingData<T>>,
) : ViewModel() {

    // dealing with compose text fields and their "multiple states" in a weird way here.
    // https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5

    private val queryFlow = MutableStateFlow("")

    val query = mutableStateOf("")
    val state = LazyGridState(0, 0)
    val snapshot = MutableStateFlow<PagingData<T>>(PagingData.empty())

    fun changeQuery(it: String = "") {
        query.value = it
        queryFlow.value = it
    }

    // exposing this does not work for me. therefore, snapshot was introduced. which works as expected.
    fun observe() = queryFlow.debounce(200).flatMapLatest { data(it).cachedIn(viewModelScope) }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch { observe().collect { snapshot.value = it } }
    }
}
