package com.moisesai.home.domain.usecase

import androidx.paging.PagingData
import com.moisesai.home.data.SongsRepository
import com.moisesai.home.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface GetSongsUseCase {
    operator fun invoke(term: String = "pop"): Flow<PagingData<Song>>
}

class GetSongsUseCaseImpl(
    private val songsRepository: SongsRepository,
) : GetSongsUseCase {
    override fun invoke(term: String): Flow<PagingData<Song>> {
        return songsRepository.getSongsPaged(term)
    }
}

