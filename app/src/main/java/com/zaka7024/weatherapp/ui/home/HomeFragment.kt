package com.zaka7024.weatherapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.zaka7024.weatherapp.R
import com.zaka7024.weatherapp.data.CityWeather
import com.zaka7024.weatherapp.databinding.FragmentHomeBinding
import com.zaka7024.weatherapp.ui.home.items.WeatherDayItem
import dagger.hilt.android.AndroidEntryPoint
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*

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
        setWeatherImage(binding)
        setDaysAdapter(binding)
    }

    private fun setCityWeatherData(binding: FragmentHomeBinding) {
        homeViewModel.cityWeather.observe(viewLifecycleOwner, { cityWeather ->
            if (cityWeather != null) {
                binding.apply {
                    weatherText.text = getString(R.string.temp_c, cityWeather.main.temp.toInt())
                    cityName.text = cityWeather.name
                    weatherStateText.text = cityWeather.weather.first().main
                    weatherDescription.text = getWeatherDescription(cityWeather)
                    dateText.text = getDate()
                }
            }
        })
    }

    private fun setWeatherImage(binding: FragmentHomeBinding) {
        homeViewModel.weatherImage.observe(viewLifecycleOwner, { weatherImageId ->
            binding.apply {
                weatherImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        weatherImageId,
                        null
                    )
                )
            }
        })
    }

    private fun setDaysAdapter(binding: FragmentHomeBinding) {
        val adapter = GroupAdapter<GroupieViewHolder>()
        binding.apply {
            daysWeatherRecycler.adapter = adapter
            daysWeatherRecycler.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )
        }

        for (i in 0.until(12)) {
            adapter.add(WeatherDayItem())
        }
    }

    private fun getWeatherDescription(cityWeather: CityWeather) = getString(
        R.string.weather_description,
        cityWeather.weather.first().description,
        cityWeather.wind.deg.toInt().toString(),
        CityWeather.windSpeedInHour(cityWeather.wind.speed).toString(),
        cityWeather.main.humidity.toInt().toString()
    ) + "%"

    private fun getDate(): String {
        val dataFormat = SimpleDateFormat("E, MMM dd", Locale.US)
        return dataFormat.format(Date())
    }
}
