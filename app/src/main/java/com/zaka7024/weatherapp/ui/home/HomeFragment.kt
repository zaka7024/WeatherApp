package com.zaka7024.weatherapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zaka7024.weatherapp.R
import com.zaka7024.weatherapp.data.CityWeather
import com.zaka7024.weatherapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel by viewModels<HomeViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHomeBinding.bind(view)

        // For test
        homeViewModel.getWeatherCity("Amman")

        //
        setCityWeatherData(binding)
    }

    private fun setCityWeatherData(binding: FragmentHomeBinding) {
        homeViewModel.cityWeather.observe(viewLifecycleOwner, { cityWeather ->

            Log.i("cityWeather", cityWeather.toString())

            binding.apply {
                weatherText.text = getString(R.string.temp_c, cityWeather.main.temp.toInt())
                cityName.text = cityWeather.name
                weatherStateText.text = cityWeather.weather.first().main
                weatherDescription.text = getWeatherDescription(cityWeather)
            }
        })
    }

    private fun getWeatherDescription(cityWeather: CityWeather) = getString(
        R.string.weather_description,
        cityWeather.weather.first().description,
        cityWeather.wind.deg.toString(),
        cityWeather.wind.speed.toString(),
        cityWeather.main.humidity.toString()
    )
}
