package com.moisesai.home.presentation.ui.album

import com.moisesai.home.domain.model.Song

sealed interface AlbumUIState {
    data object Loading : AlbumUIState
    data class Success(
        val songs: List<Song>
    ) : AlbumUIState
    data object Error : AlbumUIState
}