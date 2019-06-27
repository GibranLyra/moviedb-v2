package com.gibranlyra.moviedbservice.configuration

import com.gibranlyra.moviedbservice.model.Configuration
import io.reactivex.Single

class ConfigurationRepository(private val remoteDataSource: ConfigurationDataSource,
                              private val localDataSource: ConfigurationDataSource) : ConfigurationDataSource {

    companion object {
        private var INSTANCE: ConfigurationRepository? = null
        @JvmStatic
        fun getInstance(configurationRemoteDataSource: ConfigurationDataSource,
                        configurationLocalDataSource: ConfigurationDataSource) =
                INSTANCE ?: synchronized(ConfigurationRepository::class.java) {
                    INSTANCE ?: ConfigurationRepository(configurationRemoteDataSource, configurationLocalDataSource)
                            .also { INSTANCE = it }
                }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }

    private var cachedConfigurations: List<Configuration>? = null

    private var cacheIsDirty = false

    override fun getConfiguration(forceReload: Boolean): Single<List<Configuration>> {
        cacheIsDirty = forceReload
        if (cachedConfigurations != null && !cacheIsDirty) {
            return Single.just(cachedConfigurations)
        }
        return if (cacheIsDirty) {
            remoteDataSource
                    .getConfiguration(forceReload)
                    .flatMap { localDataSource.saveConfiguration(it.first()).toSingle { it } }
                    .doOnSuccess {
                        cacheIsDirty = false
                    }
        } else {
            localDataSource.getConfiguration(forceReload)
                    .flatMap {
                        when {
                            it.isEmpty() -> {
                                remoteDataSource.getConfiguration(forceReload)
                                        .flatMap { configuration ->
                                            localDataSource.saveConfiguration(configuration.first()).toSingle { configuration }
                                        }
                            }
                            else -> {
                                Single.just(it)
                            }
                        }
                    }
                    .map {
                        cachedConfigurations = it
                        return@map it
                    }
        }
    }
}
