package com.zaka7024.weatherapp.ui.home.items

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.zaka7024.weatherapp.R

class WeatherDayItem : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.day_weather_item
    }
}