package com.moisesai.home.domain.usecase

import com.moisesai.home.data.SongsRepository
import com.moisesai.home.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface GetSongsByAlbumUseCase {
    suspend operator fun invoke(albumId: String): Flow<List<Song>>
}

class GetSongsByAlbumUseCaseImpl(
    private val songsRepository: SongsRepository,
) : GetSongsByAlbumUseCase {
    override suspend fun invoke(albumId: String): Flow<List<Song>> {
        return songsRepository.getSongsByAlbumId(albumId)
    }
}


