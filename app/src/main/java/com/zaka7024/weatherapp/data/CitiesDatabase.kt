package com.zaka7024.weatherapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [City::class], version = 1)
abstract class CitiesDatabase: RoomDatabase() {
    abstract fun daysDao(): CityDao

    companion object {
        private var instance: CitiesDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): CitiesDatabase {
            if(instance == null) {
                synchronized(CitiesDatabase::class){
                    instance = Room.databaseBuilder(ctx.applicationContext, CitiesDatabase::class.java,
                        "cities_database")
                        .build()
                }
            }
            return instance!!
        }
    }
}