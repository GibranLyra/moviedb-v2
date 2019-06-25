package com.gibranlyra.moviedbservice.configuration

import com.gibranlyra.moviedbservice.MovieDbApiModule
import com.gibranlyra.moviedbservice.model.Configuration
import io.reactivex.Single
import timber.log.Timber

object ConfigurationApi : ConfigurationDataSource {

    private val configurationService: ConfigurationService

    init {
        val retrofit = MovieDbApiModule.retrofit
        configurationService = retrofit.create(ConfigurationService::class.java)
    }

    override fun getConfiguration(): Single<Configuration> {
        return configurationService.getConfiguration()
                .map {
                    return@map it
                }
                .doOnError { e -> Timber.e(e, "getConfiguration: %s", e.message) }
    }
}