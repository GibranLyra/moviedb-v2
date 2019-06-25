package com.gibranlyra.moviedb.di

import android.content.Context
import com.gibranlyra.moviedbservice.movie.MovieRepository
import com.gibranlyra.moviedbservice.movie.local.MovieLocalDataSource
import com.gibranlyra.moviedbservice.movie.remote.MovieRemoteDataSource
import com.gibranlyra.moviedbservice.room.MovieDataBase

object Injection {
    fun provideMovieRepository(context: Context): MovieRepository {
        val database = MovieDataBase.getInstance(context)
        return MovieRepository.getInstance(MovieRemoteDataSource, MovieLocalDataSource.getInstance(database))
    }
}