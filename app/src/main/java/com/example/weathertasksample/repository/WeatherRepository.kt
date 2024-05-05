package com.example.weathertasksample.repository

import com.example.weathertasksample.data.local.Item
import com.example.weathertasksample.data.local.WeatherDao
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val dao: WeatherDao
) {
    fun getAllWeatherItems() = dao.getAllWeatherItems()

    fun addNewWeatherItem(item: Item) = dao.addNewWeatherItem(item)
}