package com.example.weathertasksample.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weathertasksample.app.utils.Constant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = Constant.TABLE_NAME)
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "temp") val temp: String,
    @ColumnInfo(name = "humidity") val humidity: String,
    @ColumnInfo(name = "windSpeed") val windSpeed: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "imageUri") val imageUri: String,
    @ColumnInfo(name = "dateUpdated") val dateUpdated: String = getDateCreated()
)

fun getDateCreated(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))