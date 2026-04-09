package com.moisesai.player.data.remote

import com.moisesai.networking.handleError.NetworkResponse
import com.moisesai.player.data.model.ListTrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayerService {

    @GET("lookup")
    suspend fun getTrack(
        @Query("id") trackId: String
    ) : NetworkResponse<ListTrackResponse>

}