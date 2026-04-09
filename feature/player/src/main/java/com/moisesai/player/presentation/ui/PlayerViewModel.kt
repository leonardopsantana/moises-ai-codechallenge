package com.moisesai.player.presentation.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.moisesai.player.domain.usecase.GetTrackByIdUseCase
import com.moisesai.player.media.MediaPlayerWrapper
import com.moisesai.player.domain.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

private const val DEFAULT_RESOLUTION = "100x100"
private const val HIGH_RESOLUTION = "500x500"

private const val TRACK_IDS = "trackIds"

private const val CURRENT_TRACK_ID = "currentTrackId"

class PlayerViewModel(
    application: Application,
    private val getTrackByIdUseCase: GetTrackByIdUseCase,
    savedStateHandle: SavedStateHandle,
    private val mediaPlayerWrapper: MediaPlayerWrapper,
) : AndroidViewModel(application) {

    private val trackIdsString: String = savedStateHandle.get<String>(TRACK_IDS).orEmpty()
    private val currentTrackId: String = savedStateHandle.get<String>(CURRENT_TRACK_ID).orEmpty()

    private val trackIds: List<Long> = if (trackIdsString.isNotEmpty()) {
        trackIdsString.split(",").mapNotNull { it.toLongOrNull() }
    } else {
        emptyList()
    }


    private val _uiState = MutableStateFlow<PlayerUIState>(
        PlayerUIState.Success(
            currentTrack = null,
            isPlaying = false,
            currentPosition = 0L,
            duration = 0L,
            isRepeatOn = false
        )
    )
    val uiState: StateFlow<PlayerUIState> = _uiState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        PlayerUIState.Success(
            currentTrack = null,
            isPlaying = false,
            currentPosition = 0L,
            duration = 0L,
            isRepeatOn = false
        )
    )

    private val _playlistUiState = MutableStateFlow<PlaylistUiState>(
        PlaylistUiState.Loading
    )
    val playlistUiState: StateFlow<PlaylistUiState> = _playlistUiState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        PlaylistUiState.Success(playableTracks = emptyList())
    )

    init {
        loadTrack()
        observeMediaPlayer()
    }

    private fun loadTrack() {
        _playlistUiState.value = PlaylistUiState.Loading
        viewModelScope.launch {
            val currentTrackIdLong = currentTrackId.toLongOrNull()
            if (currentTrackIdLong == null) {
                _playlistUiState.value = PlaylistUiState.Error
                return@launch
            }

            val currentTrackFlow = getTrackByIdUseCase(currentTrackId)
            val otherTracksFlows = trackIds
                .filter { it != currentTrackIdLong }
                .map { trackId -> getTrackByIdUseCase(trackId.toString()) }

            if (otherTracksFlows.isEmpty()) {
                currentTrackFlow
                    .catch { _playlistUiState.value = PlaylistUiState.Error }
                    .onCompletion { _playlistUiState.value = PlaylistUiState.Success(emptyList()) }
                    .collect { tracks ->
                        if (tracks.isNotEmpty()) {
                            val currentTrack = tracks.first()
                            updateUiState { copy(currentTrack = currentTrack) }
                            currentTrack.previewUrl?.let { url ->
                                mediaPlayerWrapper.loadMedia(url)
                                playTrack(currentTrack)
                            }
                        } else {
                            _playlistUiState.value = PlaylistUiState.Error
                        }
                    }
            } else {
                combine(
                    currentTrackFlow,
                    *otherTracksFlows.toTypedArray()
                ) { results ->
                    results.toList().flatten()
                }
                    .catch { _playlistUiState.value = PlaylistUiState.Error }
                    .collect { allTracks ->
                        if (allTracks.isNotEmpty()) {
                            val currentTrack = allTracks.first()
                            updateUiState { copy(currentTrack = currentTrack) }
                            currentTrack.previewUrl?.let { url ->
                                mediaPlayerWrapper.loadMedia(url)
                                playTrack(currentTrack)
                            }
                            _playlistUiState.value = PlaylistUiState.Success(allTracks)
                        } else {
                            _playlistUiState.value = PlaylistUiState.Error
                        }
                    }
            }
        }
    }

    private fun observeMediaPlayer() {
        viewModelScope.launch {
            try {
                mediaPlayerWrapper.isPlaying.collect { isPlaying ->
                    updateUiState { copy(isPlaying = isPlaying) }
                }
            } catch (e: Exception) {
                _uiState.value = PlayerUIState.Error
            }
        }

        viewModelScope.launch {
            try {
                mediaPlayerWrapper.currentPosition.collect { position ->
                    updateUiState { copy(currentPosition = position) }

                    val currentState = _uiState.value
                    if (currentState is PlayerUIState.Success) {
                        if (currentState.duration > 0 && position >= currentState.duration - 500) {
                            if (currentState.isRepeatOn) {
                                replayCurrentTrack()
                            } else {
                                nextTrack()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = PlayerUIState.Error
            }
        }

        viewModelScope.launch {
            try {
                mediaPlayerWrapper.duration.collect { duration ->
                    updateUiState { copy(duration = duration) }
                }
            } catch (e: Exception) {
                _uiState.value = PlayerUIState.Error
            }
        }
    }

    private inline fun updateUiState(block: PlayerUIState.Success.() -> PlayerUIState.Success) {
        val currentState = _uiState.value
        if (currentState is PlayerUIState.Success) {
            _uiState.value = currentState.block()
        }
    }

    fun togglePlayPause() {
        mediaPlayerWrapper.togglePlayPause()
    }

    fun seekTo(positionMs: Long) {
        mediaPlayerWrapper.seekTo(positionMs)
    }

    fun playTrack(track: Track) {
        updateUiState { copy(currentTrack = track) }
        track.previewUrl?.let { url ->
            mediaPlayerWrapper.loadMedia(url)
            mediaPlayerWrapper.play()
        }
    }

    fun nextTrack() {
        val currentState = _uiState.value
        val playlistState = _playlistUiState.value
        if (currentState is PlayerUIState.Success && playlistState is PlaylistUiState.Success) {
            val current = currentState.currentTrack
            val currentIndex = playlistState.playableTracks.indexOfFirst { it.trackId == current?.trackId }
            if (currentIndex >= 0 && currentIndex < playlistState.playableTracks.size - 1) {
                playTrack(playlistState.playableTracks[currentIndex + 1])
            }
        }
    }

    fun previousTrack() {
        val currentState = _uiState.value
        val playlistState = _playlistUiState.value
        if (currentState is PlayerUIState.Success && playlistState is PlaylistUiState.Success) {
            val current = currentState.currentTrack
            val currentIndex = playlistState.playableTracks.indexOfFirst { it.trackId == current?.trackId }
            if (currentIndex > 0) {
                playTrack(playlistState.playableTracks[currentIndex - 1])
            }
        }
    }

    fun toggleRepeat() {
        updateUiState { copy(isRepeatOn = !isRepeatOn) }
    }

    fun replayCurrentTrack() {
        val currentState = _uiState.value
        if (currentState is PlayerUIState.Success) {
            currentState.currentTrack?.let { track ->
                playTrack(track)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerWrapper.release()
    }

    fun getArtworkUrl(): String? {
        val currentState = _uiState.value
        return if (currentState is PlayerUIState.Success) {
            currentState.currentTrack?.artworkUrl100?.replace(DEFAULT_RESOLUTION, HIGH_RESOLUTION)
        } else {
            null
        }
    }
}
