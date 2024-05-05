package com.example.weathertasksample.data.remote

import com.example.weathertasksample.data.remote.GetCurrentWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getCurrentLocationWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String
    ): Response<GetCurrentWeatherResponse>
}