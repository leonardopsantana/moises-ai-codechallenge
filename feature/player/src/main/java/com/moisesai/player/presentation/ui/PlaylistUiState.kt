package com.moisesai.player.presentation.ui

import com.moisesai.player.domain.model.Track

sealed interface PlaylistUiState {
    data object Loading : PlaylistUiState
    data class Success(
        val playableTracks: List<Track>
    ) : PlaylistUiState
    data object Error : PlaylistUiState
}

