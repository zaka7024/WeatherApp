package com.zaka7024.weatherapp.ui.home.items

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.zaka7024.weatherapp.R

class CityItem : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.city_item
    }
}