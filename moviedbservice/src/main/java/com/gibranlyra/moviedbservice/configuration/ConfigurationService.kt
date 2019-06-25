package com.gibranlyra.moviedbservice.configuration

import com.gibranlyra.moviedbservice.model.Configuration
import io.reactivex.Single
import retrofit2.http.GET

internal interface ConfigurationService {
    @GET("configuration")
    fun getConfiguration(): Single<Configuration>
}