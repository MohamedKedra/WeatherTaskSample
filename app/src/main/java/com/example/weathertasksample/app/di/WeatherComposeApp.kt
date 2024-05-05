package com.example.weathertasksample.app.di

import android.app.Application
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherComposeApp : Application() {

    fun getUriPermission(uri: Uri) {
        WeatherComposeApp().applicationContext.contentResolver.takePersistableUriPermission(
            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }
}