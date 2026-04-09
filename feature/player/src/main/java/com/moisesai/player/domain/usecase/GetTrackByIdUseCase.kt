package com.moisesai.player.domain.usecase

import com.moisesai.player.data.TrackRepository
import com.moisesai.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface GetTrackByIdUseCase {
    suspend operator fun invoke(trackId: String): Flow<List<Track>>
}

class GetTrackByIdUseCaseImpl(
    private val songsRepository: TrackRepository,
) : GetTrackByIdUseCase {
    override suspend fun invoke(trackId: String): Flow<List<Track>> {
        return songsRepository.getTrack(trackId)
    }
}


