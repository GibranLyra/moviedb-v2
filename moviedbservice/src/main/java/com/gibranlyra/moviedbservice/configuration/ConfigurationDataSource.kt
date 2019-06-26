package com.gibranlyra.moviedbservice.configuration

import com.gibranlyra.moviedbservice.model.Configuration
import io.reactivex.Single

interface ConfigurationDataSource {
    fun getConfiguration(forceReload: Boolean = false): Single<List<Configuration>>
    fun saveConfiguration(configuration: Configuration) {}
}