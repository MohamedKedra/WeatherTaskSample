package com.example.weathertasksample.repository

import com.example.weathertasksample.app.utils.Constant
import com.example.weathertasksample.data.local.Item
import com.example.weathertasksample.data.local.WeatherDao
import com.example.weathertasksample.data.remote.WeatherService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val service: WeatherService,
    private val dao: WeatherDao
) {
    fun getAllWeatherItems() = dao.getAllWeatherItems()

    fun addNewWeatherItem(item: Item) = dao.addNewWeatherItem(item)

    suspend fun getCurrentLocationWeather(lat: String, lon: String) =
        service.getCurrentLocationWeather(lat, lon, appid = Constant.apiKey)
}