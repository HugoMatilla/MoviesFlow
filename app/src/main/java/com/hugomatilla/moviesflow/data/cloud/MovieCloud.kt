package com.hugomatilla.moviesflow.data.cloud

import com.google.gson.annotations.SerializedName
import com.hugomatilla.moviesflow.data.db.Movie
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

///discover/movie?primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22


interface MovieApiService {
    @GET("/3/movie/popular")
    suspend fun getMovies(@Query("api_key") apiKey: String): MoviesResponse

    @GET("/3/movie/{id}")
    suspend fun getMovie(@Path("id") id: Long, @Query("api_key") apiKey: String): Movie
}

class MoviesResponse(
    @SerializedName("results") val movies: List<Movie>
)