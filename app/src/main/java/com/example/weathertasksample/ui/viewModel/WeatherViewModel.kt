package com.example.weathertasksample.ui.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import com.example.common.BaseViewModel
import com.example.common.LiveDataState
import com.example.weathertasksample.app.utils.ConnectionManager
import com.example.weathertasksample.data.local.Item
import com.example.weathertasksample.data.remote.GetCurrentWeatherResponse
import com.example.weathertasksample.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val connectionManager: ConnectionManager
) : BaseViewModel() {

    fun getAllWeatherItems() = repository.getAllWeatherItems()

    fun addNewWeatherItem(
        city: String,
        country: String,
        statusName: String,
        statusIcon: String,
        tempFah: String,
        tempCel: String,
        humidity: String,
        imageUri: String,
        windSpeed: String,
    ) {
        val item = Item(
            city = city,
            country= country,
            statusName = statusName,
            statusIcon = statusIcon,
            temp_fah= tempFah,
            temp_cel= tempCel,
            humidity = humidity,
            imageUri = imageUri,
            windSpeed = windSpeed
        )

        viewModelScope.launch(Dispatchers.IO){
            repository.addNewWeatherItem(item)
        }
    }

    val weatherDetailsResponse = LiveDataState<GetCurrentWeatherResponse>()

    fun getCurrentLocationWeather(lat:String,lon:String){

        publishLoading(weatherDetailsResponse)

        if (!connectionManager.isNetworkAvailable){
            publishNoInternet(weatherDetailsResponse)
            return
        }

        viewModelScope.launch {
            val response = repository.getCurrentLocationWeather(lat, lon)
            if (response.isSuccessful){
                publishResult(weatherDetailsResponse,response.body())
            }else{
                publishError(weatherDetailsResponse,response.message())
            }
        }
    }


    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }
}