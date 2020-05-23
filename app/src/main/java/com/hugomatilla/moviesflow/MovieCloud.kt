package com.hugomatilla.moviesflow

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

///discover/movie?primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22


interface MovieApiService {
  @GET("/3/movie/popular")
  suspend fun getMovies(@Query("api_key") apiKey: String): MoviesResponse
}

class MoviesResponse(
  @SerializedName("results") val movies: List<Movie>
)

data class Movie2(
  val original_title: String,
  val poster_path: String
)