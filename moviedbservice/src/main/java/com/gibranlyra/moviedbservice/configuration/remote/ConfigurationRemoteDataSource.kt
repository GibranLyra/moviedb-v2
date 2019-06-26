package com.gibranlyra.moviedbservice.configuration.remote

import com.gibranlyra.moviedbservice.MovieDbApiModule
import com.gibranlyra.moviedbservice.configuration.ConfigurationDataSource
import com.gibranlyra.moviedbservice.model.Configuration
import io.reactivex.Single
import retrofit2.http.GET
import timber.log.Timber

object ConfigurationRemoteDataSource : ConfigurationDataSource {

    private val configurationService: ConfigurationService

    init {
        val retrofit = MovieDbApiModule.retrofit
        configurationService = retrofit.create(ConfigurationService::class.java)
    }

    override fun getConfiguration(forceReload: Boolean): Single<List<Configuration>> {
        return configurationService.getConfiguration()
                .map {
                    return@map listOf(it)
                }
                .doOnError { e -> Timber.e(e, "getConfiguration: %s", e.message) }
    }

    internal interface ConfigurationService {
        @GET("configuration")
        fun getConfiguration(): Single<Configuration>
    }
}