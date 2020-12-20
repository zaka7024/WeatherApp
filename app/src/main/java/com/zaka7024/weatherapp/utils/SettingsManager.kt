package com.zaka7024.weatherapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate

class SettingsManager() {
    companion object {

        enum class AppMode {
            LightMode, NightMode
        }

        private const val DEFAULT_USER_CITY = "defaultUserCity"
        private const val APP_MODE = "appMode"

        fun setDefaultCity(context: Context, cityName: String) {
            val editor = context.getSharedPreferences("settings", MODE_PRIVATE).edit()
            editor.apply {
                putString(DEFAULT_USER_CITY, cityName)
            }
            editor.apply()
        }

        fun getDefaultCity(context: Context): String? {
            return context.getSharedPreferences("settings", MODE_PRIVATE).getString(DEFAULT_USER_CITY, null)
        }

        fun setAppMode(context: Context, appMode: AppMode) {
            if(appMode == AppMode.NightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            val editor = context.getSharedPreferences("settings", MODE_PRIVATE).edit()
            editor.apply {
                putString(APP_MODE, appMode.toString())
                apply()
            }
            editor.apply()
        }

        fun getAppMode(context: Context): AppMode {
            return when(context.getSharedPreferences("settings", MODE_PRIVATE).getString(APP_MODE, AppMode.LightMode.toString())) {
                AppMode.LightMode.toString() -> AppMode.LightMode
                AppMode.NightMode.toString() -> AppMode.NightMode
                else -> AppMode.NightMode
            }
        }
    }
}