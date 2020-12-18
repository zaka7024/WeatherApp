package com.zaka7024.weatherapp.api

import com.zaka7024.weatherapp.BuildConfig
import com.zaka7024.weatherapp.data.CityWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val APP_ID = BuildConfig.OPEN_WEATHER_KEY
    }

    @GET("weather")
    suspend fun getCityWeather(
        @Query("q") cityName: String,
        @Query("appid") appId: String,
        @Query("units") units: String = "metric"
    ): CityWeather
}