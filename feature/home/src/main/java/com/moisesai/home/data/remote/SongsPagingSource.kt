package com.moisesai.home.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.moisesai.home.domain.model.Song
import com.moisesai.home.domain.model.toListSongs

class SongsPagingSource(
    private val songsService: SongsService,
    private val searchTerm: String
) : PagingSource<Int, Song>() {

    override fun getRefreshKey(state: PagingState<Int, Song>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song> {
        return try {
            val page = params.key ?: 0
            val offset = page * PAGE_SIZE

            val response = songsService.getSongs(
                term = searchTerm,
                limit = PAGE_SIZE,
                offset = offset
            )

            val songs = when (response) {
                is com.moisesai.networking.handleError.NetworkResponse.Success -> {
                    response.value.toListSongs()
                }
                else -> emptyList()
            }

            LoadResult.Page(
                data = songs,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (songs.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}

