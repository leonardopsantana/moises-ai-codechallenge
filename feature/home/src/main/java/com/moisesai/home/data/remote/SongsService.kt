package com.moisesai.home.data.remote

import com.moisesai.home.data.model.ListSongsResponse
import com.moisesai.networking.handleError.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SongsService {
    @GET("search?entity=song")
    suspend fun getSongs(
        @Query("term") term: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ) : NetworkResponse<ListSongsResponse>

    @GET("lookup?entity=song")
    suspend fun getSongsByCollectionId(
        @Query("id") collectionId: String
    ) : NetworkResponse<ListSongsResponse>

}