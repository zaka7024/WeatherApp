package com.zaka7024.weatherapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zaka7024.weatherapp.R
import com.zaka7024.weatherapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHomeBinding.bind(view)

        homeViewModel.cityWeather.observe(viewLifecycleOwner, {
            Log.i("homeViewModel", "CityWeather: $it")
        })

        homeViewModel.getWeatherCity("Amman")
    }
}