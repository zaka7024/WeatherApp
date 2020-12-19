package com.zaka7024.weatherapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val cityName: String,
    val cityTemperature: Int = 0,
    val weatherIcon: String = ""
)