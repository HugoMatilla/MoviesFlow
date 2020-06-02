package com.hugomatilla.moviesflow.data.cloud

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.themoviedb.org/"
const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

class CloudService {

    fun createMovieApis(): MovieApiService = getRestService().create(MovieApiService::class.java)
    fun createSessionApi(): SessionApiService = getRestService().create(SessionApiService::class.java)

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
