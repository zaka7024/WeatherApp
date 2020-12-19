package com.zaka7024.weatherapp.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.zaka7024.weatherapp.R
import com.zaka7024.weatherapp.data.City
import com.zaka7024.weatherapp.data.CityDao
import com.zaka7024.weatherapp.data.CityWeather
import com.zaka7024.weatherapp.data.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeViewModel @ViewModelInject constructor(
    private val weatherRepository: WeatherRepository
) :
    ViewModel() {

    private val _cityWeather = MutableLiveData<CityWeather>()
    val cityWeather: LiveData<CityWeather>
        get() = _cityWeather

    private val _userCities = weatherRepository.userStoredCities
    val userCities: LiveData<List<City>>
        get() = _userCities

    private val _weatherImage = _cityWeather.switchMap {
        MutableLiveData(getWeatherImage(it))
    }

    val weatherImage: LiveData<Int>
        get() = _weatherImage

    fun getWeatherCity(cityName: String) {
        viewModelScope.launch {
            val weather = withContext(Dispatchers.Default) {
                weatherRepository.getWeatherCity(cityName)
            }
            if (weather != null)
                _cityWeather.value = weather
        }
    }

    fun saveUserCity(city: City) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                weatherRepository.saveCity(city)
            }
        }
    }

    private fun getWeatherImage(cityWeather: CityWeather): Int {
        return when (cityWeather.weather.first().main.toLowerCase(Locale.ENGLISH)) {
            "thunderstorm" -> R.drawable.thunderstorm
            "drizzle" -> R.drawable.drizzle
            "rain" -> R.drawable.rain
            "haze" -> R.drawable.haze
            "snow" -> R.drawable.snow
            "clear" -> R.drawable.weather_sunny
            "clouds" -> R.drawable.cloudy
            else -> R.drawable.weather_sunny
        }
    }
}