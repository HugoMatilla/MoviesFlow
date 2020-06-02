package com.hugomatilla.moviesflow.data.cloud

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface SessionApiService {
//    @GET("/3/authentication/guest_session/new")
//    suspend fun getGuestSession(@Query("api_key") apiKey: String ): SessionId

    @GET("/3/discover/movie?sort_by=vote_average.desc")
    suspend fun getRandomMovie(
        @Query("api_key") apiKey: String,
        @Query("primary_release_year") year: String
    ): MoviesResponse
}

data class SessionId(@SerializedName("guest_session_id") val id: String)