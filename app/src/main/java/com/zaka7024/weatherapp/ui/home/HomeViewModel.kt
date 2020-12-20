package com.zaka7024.weatherapp.ui.home

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.zaka7024.weatherapp.data.City
import com.zaka7024.weatherapp.data.CityWeather
import com.zaka7024.weatherapp.data.WeatherRepository
import com.zaka7024.weatherapp.ui.home.items.OnCiteItemClickOptionsListener
import com.zaka7024.weatherapp.utils.SettingsManager
import kotlinx.coroutines.*

class HomeViewModel @ViewModelInject constructor(
    private val weatherRepository: WeatherRepository,
    application: Application
) :
    AndroidViewModel(application) , OnCiteItemClickOptionsListener {

    private val _cityWeather = MutableLiveData<CityWeather>()
    val cityWeather: LiveData<CityWeather>
        get() = _cityWeather

    private val _userCities = weatherRepository.userStoredCities
    val userCities: LiveData<List<City>>
        get() = _userCities


    private var updated = false

    private val _weatherImage = _cityWeather.switchMap {
        MutableLiveData(CityWeather.getWeatherImage(it.weather.first().main))
    }

    val weatherImage: LiveData<Int>
        get() = _weatherImage

    init {
        val defaultCity = SettingsManager.getDefaultCity(application)
        getWeatherCity(defaultCity?: "Amman")
    }

    fun getWeatherCity(cityName: String) {
        viewModelScope.launch {
            val weather = withContext(Dispatchers.IO) {
                weatherRepository.getWeatherCity(cityName)
            }
            if (weather != null)
                _cityWeather.value = weather
        }
    }

    fun saveUserCity(city: City) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val cityWeather = weatherRepository.getWeatherCity(city.cityName)
                if (cityWeather != null) {
                    val newCity = City(
                        city.cityName,
                        cityWeather.main.temp.toInt(),
                        cityWeather.weather.first().main,
                        cityWeather.weather.first().icon
                    )
                    weatherRepository.saveCity(newCity)
                }
            }
        }
    }

    fun selectMainCity(cityName: String) {
        getWeatherCity(cityName)
        // Change the default city
        SettingsManager.setDefaultCity(getApplication(), cityName)
    }

    private fun deleteCity(city: City) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                weatherRepository.deleteCity(city)
            }
        }
    }

    fun updateCitiesWeather() {
        if (updated) return
        updated = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (_userCities.value != null) {
                    for (city in _userCities.value!!) {
                        val cityWeather =
                            withContext(Dispatchers.IO) {
                                weatherRepository.getWeatherCity(city.cityName)
                            }
                        if (cityWeather != null) {
                            val newCity = City(
                                city.cityName,
                                cityWeather.main.temp.toInt(),
                                cityWeather.weather.first().main,
                                cityWeather.weather.first().icon
                            )
                            weatherRepository.updateCity(newCity)
                        }
                    }
                }
            }
        }
    }

    override fun onDeleteOption(city: City) {
        deleteCity(city)
    }
}