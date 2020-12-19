package com.zaka7024.weatherapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(city: City)

    @Delete
    fun delete(city: City)

    @Update
    fun update(city: City)

    @Query("select * from cities")
    fun getAllCities() : LiveData<List<City>>
}