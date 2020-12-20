package com.zaka7024.weatherapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

class SettingsManager() {
    companion object {

        const val DEFAULT_USER_CITY = "defaultUserCity"

        fun setDefaultCity(context: Context, cityName: String) {
            val editor = context.getSharedPreferences("settings", MODE_PRIVATE).edit()
            editor.apply {
                putString(DEFAULT_USER_CITY, cityName)
                apply()
            }
        }

        fun getDefaultCity(context: Context): String? {
            return context.getSharedPreferences("settings", MODE_PRIVATE).getString(DEFAULT_USER_CITY, null)
        }
    }
}