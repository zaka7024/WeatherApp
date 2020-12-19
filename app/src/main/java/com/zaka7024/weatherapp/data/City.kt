package com.zaka7024.weatherapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
    @PrimaryKey()
    val cityName: String,
    val cityTemperature: Int = 0,
    val weatherCondition: String = "rain",
    val weatherIcon: String = ""
)