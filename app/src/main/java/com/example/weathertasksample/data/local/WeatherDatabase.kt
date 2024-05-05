package com.example.weathertasksample.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weathertasksample.data.local.WeatherDao

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}