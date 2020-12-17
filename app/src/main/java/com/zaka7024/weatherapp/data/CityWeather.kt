package com.zaka7024.weatherapp.data

data class CityWeather(
    val weather: List<WeatherCondition>,
    val main: Temperature,
    val wind: Wind,
    val clouds: Clouds,
    val name: String
    ) {
    data class WeatherCondition(
        val description: String
    )

    data class Temperature(
        val temp: Float,
        val pressure: Float,
        val humidity: Float,
        val temp_min: Float,
        val temp_max: Float
    )

    data class Wind(
        val speed: Float,
        val deg: Float
    )

    data class Clouds(
        val all: Float
    )
}