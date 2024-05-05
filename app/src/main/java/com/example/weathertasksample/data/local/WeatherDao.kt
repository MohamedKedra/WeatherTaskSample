package com.example.weathertasksample.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNewWeatherItem(item: Item)

    @Query("SELECT * from weather_items")
    fun getAllWeatherItems(): LiveData<List<Item>>
}