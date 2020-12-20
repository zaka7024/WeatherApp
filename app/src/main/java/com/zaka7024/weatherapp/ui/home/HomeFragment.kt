package com.zaka7024.weatherapp.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
import com.zaka7024.weatherapp.data.City
import com.zaka7024.weatherapp.data.CityWeather
import com.zaka7024.weatherapp.databinding.FragmentHomeBinding
import com.zaka7024.weatherapp.ui.home.items.CityItem
import com.zaka7024.weatherapp.ui.home.items.WeatherDayItem
import com.zaka7024.weatherapp.utils.SettingsManager
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHomeBinding.bind(view)

        // Set app mode icon
        val appMode = SettingsManager.getAppMode(requireContext())
        if(appMode == SettingsManager.Companion.AppMode.LightMode) {
            binding.appModeIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_nights_stay_24))
        }else {
            binding.appModeIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_wb_sunny_24))
        }

        binding.drawerIcon.setOnClickListener {
            it.isVisible = false
            binding.appModeIcon.isVisible = false
            openDrawer(binding)
        }

        binding.appModeIcon.setOnClickListener {icon ->
            if(appMode == SettingsManager.Companion.AppMode.NightMode) {
                binding.appModeIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_nights_stay_24))
                SettingsManager.setAppMode(requireContext(), SettingsManager.Companion.AppMode.LightMode)
            }else {
                binding.appModeIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_wb_sunny_24))
                SettingsManager.setAppMode(requireContext(), SettingsManager.Companion.AppMode.NightMode)
            }
        }

        //
        setCurrentTime(binding)
        setCityWeatherData(binding)
        setCityAdapter(binding)
        setWeatherImage(binding)
        setDaysAdapter(binding)
    }

    private fun setCurrentTime(binding: FragmentHomeBinding) {
        binding.apply {
            currentTimeText.text = getCurrentTime()
        }
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

                                // Show all views
                                currentTimeText.fadeInTop()
                                drawerIcon.fadeInTop()
                                appModeIcon.fadeInTop()

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

    private fun openDrawer(binding: FragmentHomeBinding) {
        binding.apply {
            appDrawer.drawerAddIcon.setOnClickListener {
                showSelectCityDialog()
            }

            appDrawer.drawerCloseIcon.setOnClickListener {
                closeTheDrawer(binding)
            }
            appDrawer.drawer.isVisible = true
            appDrawer.drawer.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.drawer_open
                )
            )
        }
    }

    private fun closeTheDrawer(binding: FragmentHomeBinding) {
        val closeAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.drawer_close)
        closeAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                binding.drawerIcon.fadeInTop()
                binding.appModeIcon.fadeInTop()
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.appDrawer.drawer.isVisible = false
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        binding.appDrawer.drawer.startAnimation(closeAnimation)
    }

    private fun View.fadeInBottom() {
        this.isVisible = true
        val fadeInBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_bottom)
        this.startAnimation(fadeInBottom)
    }

    private fun View.fadeInBottomDelayed() {
        this.isVisible = true
        val fadeInBottom =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_bottom_delayed)
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

    private fun setCityAdapter(binding: FragmentHomeBinding) {
        val adapter = GroupAdapter<GroupieViewHolder>()
        binding.apply {
            binding.appDrawer.citiesRecycler.adapter = adapter
        }

        homeViewModel.userCities.observe(viewLifecycleOwner, {
            homeViewModel.updateCitiesWeather()
            adapter.clear()
            if(it.isEmpty()) {
                binding.appDrawer.noCitiesHint.isVisible = true
            }else {
                binding.appDrawer.noCitiesHint.isVisible = false
                it.forEach { city ->
                    adapter.add(CityItem(city, requireContext(), action = {cityName ->
                        homeViewModel.selectMainCity(cityName)
                        closeTheDrawer(binding)
                    }, homeViewModel))
                }
            }
        })
    }

    private fun getWeatherDescription(cityWeather: CityWeather) = getString(
        R.string.weather_description,
        cityWeather.weather.first().description,
        cityWeather.wind.deg.toInt().toString(),
        CityWeather.windSpeedInHour(cityWeather.wind.speed).roundToInt().toString(),
        cityWeather.main.humidity.toInt().toString()
    ) + "%"

    private fun getDate(): String {
        val dataFormat = SimpleDateFormat("E, MMM dd", Locale.US)
        return dataFormat.format(Date())
    }

    private fun getCurrentTime(): String {
        return SimpleDateFormat("h:mm a").format(Calendar.getInstance().time)
    }

    private fun showSelectCityDialog() {
        val dialog = Dialog(requireContext())
        dialog.apply {
            setContentView(R.layout.search_city_dialog)
            val search = findViewById<AppCompatAutoCompleteTextView>(R.id.auto_search_view)
            val countryList = resources.getStringArray(R.array.countries_array)
            search.setAdapter(
                ArrayAdapter(
                    requireContext(), R.layout.auto_complete_item, R.id.auto_complete_text,
                    countryList
                )
            )

            val window: Window = window!!
            window.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // Try select the country
            val addCountryButton = findViewById<Button>(R.id.add_country)
            addCountryButton.setOnClickListener {
                val selectedText = search.text.toString()
                if (countryList.contains(selectedText)) {
                    // Save the selected item to cities table
                    homeViewModel.saveUserCity(City(cityName = selectedText))
                    dismiss()
                }
            }

            show()
        }
    }
}
