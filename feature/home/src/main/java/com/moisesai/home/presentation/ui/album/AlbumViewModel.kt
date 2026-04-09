package com.moisesai.home.presentation.ui.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moisesai.home.domain.usecase.GetSongsByAlbumUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val getSongsByAlbumUseCase: GetSongsByAlbumUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val albumId: String = savedStateHandle.get<String>("albumId") ?: ""

    private val _uiState = MutableStateFlow<AlbumUIState>(
        AlbumUIState.Loading
    )
    val uiState = _uiState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        initialValue = _uiState.value
    )

    init {
        loadAlbumSongs()
    }

    private fun loadAlbumSongs() {
        viewModelScope.launch {
            try {
                getSongsByAlbumUseCase(albumId)
                    .collect { songs ->
                        if (songs.isNotEmpty()) {
                            _uiState.update {
                                AlbumUIState.Success(
                                    songs = songs.drop(1)
                                )
                            }
                        } else {
                            _uiState.update { AlbumUIState.Error }
                        }
                    }
            } catch (_: Exception) {
                _uiState.update { AlbumUIState.Error }
            }
        }
    }
}