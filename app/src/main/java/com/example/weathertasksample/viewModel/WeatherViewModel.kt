package com.example.weathertasksample.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertasksample.data.local.Item
import com.example.weathertasksample.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    val items: LiveData<List<Item>> = repository.getAllWeatherItems()

    fun addNewWeatherItem(
        city: String,
        status: String,
        temp: String,
        humidity: String,
        imageUri: String,
        windSpeed: String
    ) {
        val item = Item(
            city = city,
            status = status,
            temp = temp,
            humidity = humidity,
            imageUri = imageUri,
            windSpeed = windSpeed
        )

        viewModelScope.launch(Dispatchers.IO){
            repository.addNewWeatherItem(item)
        }
    }
}