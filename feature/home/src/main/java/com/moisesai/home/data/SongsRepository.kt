package com.moisesai.home.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.moisesai.home.data.remote.SongsPagingSource
import com.moisesai.home.data.remote.SongsService
import com.moisesai.home.domain.model.Song
import com.moisesai.home.domain.model.toListSongs
import com.moisesai.networking.handleError.toFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SongsRepository {

    suspend fun getSongsByAlbumId(albumId: String): Flow<List<Song>>

    fun getSongsPaged(term: String): Flow<PagingData<Song>>
}

class SongsRepositoryImpl(
    private val songsService: SongsService
) : SongsRepository {


    override fun getSongsPaged(term: String): Flow<PagingData<Song>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = MAX_SIZE
            ),
            pagingSourceFactory = {
                SongsPagingSource(songsService, term)
            }
        ).flow
    }

    override suspend fun getSongsByAlbumId(albumId: String): Flow<List<Song>> {
        return songsService.getSongsByCollectionId(albumId).toFlow().map { it.toListSongs() }
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val MAX_SIZE = 200
    }
}
