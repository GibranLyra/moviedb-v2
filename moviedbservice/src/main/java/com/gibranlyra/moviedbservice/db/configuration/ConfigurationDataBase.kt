package com.gibranlyra.moviedbservice.db.configuration

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gibranlyra.moviedbservice.db.Converters
import com.gibranlyra.moviedbservice.model.Configuration

@Database(entities = [Configuration::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ConfigurationDataBase : RoomDatabase() {
    abstract fun configurationDao(): ConfigurationDao

    companion object {
        private var INSTANCE: ConfigurationDataBase? = null

        private val lock = Any()

        fun getInstance(context: Context): ConfigurationDataBase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, ConfigurationDataBase::class.java, "Configuration.db")
                            .build()
                }
                return INSTANCE!!
            }
        }
    }
}