package com.zaka7024.weatherapp.ui.home.items

import android.content.Context
import android.widget.PopupMenu
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.zaka7024.weatherapp.R
import com.zaka7024.weatherapp.data.City
import com.zaka7024.weatherapp.data.CityWeather
import com.zaka7024.weatherapp.data.WeatherRepository
import com.zaka7024.weatherapp.databinding.CityItemBinding
import com.zaka7024.weatherapp.utils.SettingsManager
import kotlinx.coroutines.*
import kotlin.math.roundToInt

interface OnCiteItemClickOptionsListener {
    fun onDeleteOption(city: City)
}

class CityItem(
    private val city: City,
    private val context: Context,
    private val action: (String) -> Unit,
    private val onCiteItemClickOptionsListener: OnCiteItemClickOptionsListener
) : Item<GroupieViewHolder>() {

    private var binding: CityItemBinding? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        binding = CityItemBinding.bind(viewHolder.itemView)
        binding?.apply {
            cityName.text = city.cityName
            cityWeather.text = city.cityTemperature.toString()

            imageView.setImageResource(CityWeather.getWeatherImage(city.weatherCondition))

            // load weather condition icon
            Glide.with(context)
                .load(CityWeather.getWeatherIcon(city.weatherIcon))
                .into(cityWeatherIcon)

            // Show popup menu options
            cityOptionsIcon.setOnClickListener {
                val menu = PopupMenu(context, it)
                menu.inflate(R.menu.city_options)
                menu.show()
                menu.setOnMenuItemClickListener {item ->
                    if (item.itemId == R.id.delete_city) {
                        onCiteItemClickOptionsListener.onDeleteOption(city)
                    }
                    true
                }
            }

            // Change main user city
            root.setOnClickListener {
                action(city.cityName)
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.city_item
    }
}
