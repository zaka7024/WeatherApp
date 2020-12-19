package com.zaka7024.weatherapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CityDao {

    @Insert
    fun insert(city: City)

    @Delete
    fun delete(city: City)

    @Query("select * from cities")
    fun getAllCities() : LiveData<List<City>>
}