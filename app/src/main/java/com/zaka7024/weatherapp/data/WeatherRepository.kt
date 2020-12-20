package com.zaka7024.weatherapp.data

import android.util.Log
import com.zaka7024.weatherapp.api.WeatherApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val cityDao: CityDao
) {

    val userStoredCities = cityDao.getAllCities()

    suspend fun getWeatherCity(city: String): CityWeather? {
        return try {
            return weatherApi.getCityWeather(cityName = city, appId = WeatherApi.APP_ID)
        } catch (e: IOException) {
            Log.i("WeatherRepository", e.toString())
            null
        } catch (e: HttpException) {
            Log.i("WeatherRepository", e.toString())
            null
        }
    }

    suspend fun saveCity(city: City) {
        cityDao.insert(city)
    }

    suspend fun updateCity(city: City) {
        cityDao.update(city)
    }

    suspend fun deleteCity(city: City) {
        cityDao.delete(city)
    }
}