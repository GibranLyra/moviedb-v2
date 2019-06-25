package com.gibranlyra.moviedbservice.movie.local

import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.movie.MovieDataSource
import com.gibranlyra.moviedbservice.room.MovieDbDataBase
import io.reactivex.Single

class MovieLocalDataSource(private val movieDbDataBase: MovieDbDataBase) : MovieDataSource {
    override fun getMovies(forceReload: Boolean, page: Int) = Single.fromCallable { movieDbDataBase.movieDao().getAll() }

    override fun saveMovies(movies: List<Movie>) {
        movieDbDataBase.movieDao().deleteAll()
        movieDbDataBase.movieDao().insert(movies)
    }

    override fun getMovie(movieId: String) = Single.fromCallable { movieDbDataBase.movieDao().findById(movieId) }
}