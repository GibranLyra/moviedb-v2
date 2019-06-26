package com.gibranlyra.moviedbservice.configuration.local

import androidx.annotation.VisibleForTesting
import com.gibranlyra.moviedbservice.configuration.ConfigurationDataSource
import com.gibranlyra.moviedbservice.db.configuration.ConfigurationDataBase
import com.gibranlyra.moviedbservice.model.Configuration
import io.reactivex.Single

class ConfigurationLocalDataSource private constructor(private val configurationDataBase: ConfigurationDataBase)
    : ConfigurationDataSource {

    companion object {
        private var INSTANCE: ConfigurationLocalDataSource? = null

        @JvmStatic
        fun getInstance(configurationDataBase: ConfigurationDataBase): ConfigurationLocalDataSource {
            if (INSTANCE == null) {
                synchronized(ConfigurationLocalDataSource::javaClass) {
                    INSTANCE = ConfigurationLocalDataSource(configurationDataBase)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }

    override fun getConfiguration(forceReload: Boolean): Single<List<Configuration>> =
            Single.fromCallable { configurationDataBase.configurationDao().get() }

    override fun saveConfiguration(configuration: Configuration) {
        configurationDataBase.configurationDao().deleteAll()
        configurationDataBase.configurationDao().insert(configuration)
    }
}