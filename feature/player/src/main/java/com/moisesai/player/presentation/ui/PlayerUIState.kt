package com.moisesai.player.presentation.ui

import com.moisesai.player.domain.model.Track

sealed interface PlayerUIState {
    data object Loading : PlayerUIState
    data class Success(
        val currentTrack: Track?,
        val isPlaying: Boolean,
        val currentPosition: Long,
        val duration: Long,
        val isRepeatOn: Boolean
    ) : PlayerUIState
    data object Error : PlayerUIState
}