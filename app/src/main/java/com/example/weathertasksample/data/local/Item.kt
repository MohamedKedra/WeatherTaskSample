package com.example.weathertasksample.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weathertasksample.app.utils.Constant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Random
import java.util.UUID

@Entity(tableName = Constant.TABLE_NAME)
data class Item(
    @PrimaryKey val id: String = getRandomString(6),
    @ColumnInfo(name = "city") val city: String = "",
    @ColumnInfo(name = "country") val country: String="",
    @ColumnInfo(name = "temp_fah") val temp_fah: String="",
    @ColumnInfo(name = "temp_cel") val temp_cel: String="",
    @ColumnInfo(name = "humidity") val humidity: String="",
    @ColumnInfo(name = "windSpeed") val windSpeed: String="",
    @ColumnInfo(name = "status_name") val statusName: String="",
    @ColumnInfo(name = "status_icon") val statusIcon: String="",
    @ColumnInfo(name = "imageUri") val imageUri: String="",
    @ColumnInfo(name = "dateUpdated") val dateUpdated: String = getDateCreated()
)

fun getDateCreated(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}