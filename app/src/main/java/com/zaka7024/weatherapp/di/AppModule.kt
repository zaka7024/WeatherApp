package com.zaka7024.weatherapp.di

import android.content.Context
import com.zaka7024.weatherapp.api.WeatherApi
import com.zaka7024.weatherapp.data.CitiesDatabase
import com.zaka7024.weatherapp.data.CityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WeatherApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): CitiesDatabase {
        return CitiesDatabase.getInstance(appContext)
    }

    @Provides
    @Singleton
    fun provideCityDao(citiesDatabase: CitiesDatabase): CityDao {
        return citiesDatabase.daysDao()
    }
}
