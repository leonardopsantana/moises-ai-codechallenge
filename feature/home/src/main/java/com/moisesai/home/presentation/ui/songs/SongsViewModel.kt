package com.moisesai.home.presentation.ui.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.moisesai.home.domain.model.Song
import com.moisesai.home.domain.usecase.GetSongsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

private const val DEBOUNCE_TIME = 300L
private const val DEFAULT_TERM = "pop"

class SongsViewModel(
    private val getSongsUseCase: GetSongsUseCase,
) : ViewModel() {

    private val _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        initialValue = _searchTerm.value
    )

    private val _songsPagingData = MutableStateFlow<Flow<PagingData<Song>>>(
        getSongsUseCase(DEFAULT_TERM).cachedIn(viewModelScope)
    )
    val songsPagingData = _songsPagingData.asStateFlow()

    init {
        _searchTerm
            .debounce(DEBOUNCE_TIME)
            .distinctUntilChanged()
            .onEach { term ->
                if (term.isNotBlank()) {
                    _songsPagingData.value = getSongsUseCase(term).cachedIn(viewModelScope)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchTermChanged(term: String) {
        _searchTerm.update { term }
    }
}
