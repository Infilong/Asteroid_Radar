package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfTheDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = API_KEY,
    ): String
}

interface PictureOfTheDayApiService {
    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(
        @Query("api_key") apiKey: String = API_KEY,
    ): PictureOfTheDay
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()


object AsteroidApi {
    val retrofitServiceAsteroids: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
    val retrofitServicePictureOfTheDay: PictureOfTheDayApiService by lazy {
        retrofit.create(PictureOfTheDayApiService::class.java)
    }
}

