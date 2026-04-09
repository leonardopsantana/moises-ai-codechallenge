package com.moisesai.player.data

import com.moisesai.networking.handleError.toFlow
import com.moisesai.player.data.remote.PlayerService
import com.moisesai.player.domain.model.Track
import com.moisesai.player.domain.model.toListTracks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TrackRepository {
    suspend fun getTrack(albumId: String): Flow<List<Track>>
}

class TrackRepositoryImpl(
    private val playerService: PlayerService
) : TrackRepository {

    override suspend fun getTrack(trackId: String): Flow<List<Track>> {
        return playerService.getTrack(trackId).toFlow().map { it.toListTracks() }
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val MAX_SIZE = 500
    }
}
