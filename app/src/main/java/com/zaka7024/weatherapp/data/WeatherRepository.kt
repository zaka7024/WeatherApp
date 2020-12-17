package com.zaka7024.weatherapp.data

import android.util.Log
import com.zaka7024.weatherapp.api.WeatherApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherApi: WeatherApi){

    suspend fun getWeatherCity(city: String): CityWeather? {
        return try {
            return weatherApi.getCityWeather(city,  WeatherApi.APP_ID)
        }
        catch (e: IOException) {
            Log.i("WeatherRepository", e.toString())
            null
        }
        catch (e: HttpException) {
            Log.i("WeatherRepository", e.toString())
            null
        }
    }
}