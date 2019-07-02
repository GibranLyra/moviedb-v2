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
import com.gibranlyra.moviedbservice.search.SearchRepository
import com.gibranlyra.moviedbservice.search.local.SearchLocalDataSource
import com.gibranlyra.moviedbservice.search.remote.SearchRemoteDataSource

object Injection {
    fun provideConfigurationRepository(context: Context): ConfigurationRepository {
        val database = ConfigurationDataBase.getInstance(context)
        return ConfigurationRepository.getInstance(ConfigurationRemoteDataSource, ConfigurationLocalDataSource.getInstance(database))
    }

    fun provideMovieRepository(context: Context): MovieRepository {
        val database = MovieDataBase.getInstance(context)
        return MovieRepository.getInstance(MovieRemoteDataSource, MovieLocalDataSource.getInstance(database))
    }

    fun provideSearchRepository(context: Context): SearchRepository {
        val database = MovieDataBase.getInstance(context)
        return SearchRepository.getInstance(SearchRemoteDataSource, SearchLocalDataSource.getInstance(database))
    }

}
