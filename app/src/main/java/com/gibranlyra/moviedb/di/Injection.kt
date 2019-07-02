package com.gibranlyra.moviedb.di

import android.content.Context
import com.gibranlyra.moviedbservice.configuration.ConfigurationRepository
import com.gibranlyra.moviedbservice.configuration.local.ConfigurationLocalDataSource
import com.gibranlyra.moviedbservice.configuration.remote.ConfigurationRemoteDataSource
import com.gibranlyra.moviedbservice.db.configuration.ConfigurationDataBase
import com.gibranlyra.moviedbservice.db.movie.MovieDataBase
import com.gibranlyra.moviedbservice.movie.MovieRepository
import com.gibranlyra.moviedbservice.movie.local.MovieLocalDataSource
import com.gibranlyra.moviedbservice.movie.remote.MovieRemoteDataSource
import com.gibranlyra.moviedbservice.search.SearchApi

object Injection {
    fun provideConfigurationRepository(context: Context): ConfigurationRepository {
        val database = ConfigurationDataBase.getInstance(context)
        return ConfigurationRepository.getInstance(ConfigurationRemoteDataSource, ConfigurationLocalDataSource.getInstance(database))
    }

    fun provideMovieRepository(context: Context): MovieRepository {
        val database = MovieDataBase.getInstance(context)
        return MovieRepository.getInstance(MovieRemoteDataSource, MovieLocalDataSource.getInstance(database))
    }

    fun provideSearchRepository(): SearchApi {
        return SearchApi
    }

}
