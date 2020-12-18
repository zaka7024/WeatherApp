package com.zaka7024.weatherapp.ui.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.zaka7024.weatherapp.R
import com.zaka7024.weatherapp.data.CityWeather
import com.zaka7024.weatherapp.databinding.FragmentHomeBinding
import com.zaka7024.weatherapp.ui.home.items.WeatherDayItem
import dagger.hilt.android.AndroidEntryPoint
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

        // Set current time
        binding.apply {
            currentTimeText.text = getCurrentTime()
        }

        //
        setCityWeatherData(binding)
        setWeatherImage(binding)
        setDaysAdapter(binding)
    }

    private fun setCityWeatherData(binding: FragmentHomeBinding) {
        homeViewModel.cityWeather.observe(viewLifecycleOwner, { cityWeather ->
            if (cityWeather != null) {
                binding.apply {

                    val weather = cityWeather.weather.first()

                    weatherText.text = getString(R.string.temp_c, cityWeather.main.temp.toInt())
                    minMaxTempText.text = getString(
                        R.string.max_min_temp, cityWeather.main.temp_max.toInt(),
                        cityWeather.main.temp_min.toInt()
                    )
                    cityName.text = cityWeather.name
                    weatherStateText.text = weather.main
                    weatherDescription.text = getWeatherDescription(cityWeather)
                    dateText.text = getDate()

                    // load the icon
                    Glide.with(this@HomeFragment)
                        .load(CityWeather.getWeatherIcon(weather.icon))
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {

                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {

                                currentTimeText.fadeInTop()
                                drawerIcon.fadeInTop()

                                weatherImage.fadeInBottom()
                                weatherText.fadeInBottom()
                                minMaxTempText.fadeInBottom()
                                weatherConditionIcon.scaleIn()

                                dateText.fadeInBottomDelayed()
                                cityName.fadeInBottomDelayed()
                                weatherStateText.fadeInBottomDelayed()
                                weatherDescription.fadeInBottomDelayed()

                                daysWeatherRecycler.scaleIn()
                                return false
                            }
                        })
                        .into(weatherConditionIcon)
                }
            }
        })
    }

    private fun View.fadeInBottom() {
        this.isVisible = true
        val fadeInBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_bottom)
        this.startAnimation(fadeInBottom)
    }

    private fun View.fadeInBottomDelayed() {
        this.isVisible = true
        val fadeInBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_bottom_delayed)
        this.startAnimation(fadeInBottom)
    }

    private fun View.fadeInTop() {
        this.isVisible = true
        val fadeInTop = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_top)
        this.startAnimation(fadeInTop)
    }

    private fun View.scaleIn() {
        this.isVisible = true
        val scaleIn = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_in)
        this.startAnimation(scaleIn)
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

    private fun getCurrentTime(): String {
        return SimpleDateFormat("h:mm a").format(Calendar.getInstance().time)
    }
}
