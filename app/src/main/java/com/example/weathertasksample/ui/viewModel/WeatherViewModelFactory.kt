package com.example.weathertasksample.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weathertasksample.app.utils.ConnectionManager
import com.example.weathertasksample.data.repository.WeatherRepository

class WeatherViewModelFactory(
    private val repository: WeatherRepository,
    private val connectionManager: ConnectionManager
) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(repository,connectionManager) as T
    }
}