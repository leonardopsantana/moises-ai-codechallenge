package com.moisesai.home.presentation.ui.songs

import com.moisesai.home.domain.model.Song


sealed interface SongsUIState {
    data object Loading : SongsUIState
    data class Success(val songs: List<Song>) : SongsUIState
    data object Error : SongsUIState
}