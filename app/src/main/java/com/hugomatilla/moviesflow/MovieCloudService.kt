package com.hugomatilla.moviesflow

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.themoviedb.org/"
const val API_KEY = "093aa515aa2aac842743586209c0319f"
const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

class MovieCloudServiceImpl {

  fun create(): MovieApiService = getRestService().create(MovieApiService::class.java)

  private fun getRestService(): Retrofit {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val okHttpClient = OkHttpClient().newBuilder()
    okHttpClient.addInterceptor(loggingInterceptor)

    return Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create())
      .client(okHttpClient.build())
      .baseUrl(BASE_URL)
      .build()
  }
}
