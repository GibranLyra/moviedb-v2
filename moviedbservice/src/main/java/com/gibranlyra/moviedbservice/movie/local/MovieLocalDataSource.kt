package com.gibranlyra.moviedbservice.movie.local

import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.movie.MovieDataSource
import com.gibranlyra.moviedbservice.room.MovieDbDataBase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MovieLocalDataSource(private val movieDbDataBase: MovieDbDataBase) : MovieDataSource {
    override fun getMovies(forceReload: Boolean, page: Int): Single<List<Movie>> {
        return Single
                .fromCallable { movieDbDataBase.movieDao().getAll() }
                .observeOn(Schedulers.io())
    }

    override fun saveMovies(movies: List<Movie>) {
        movieDbDataBase.movieDao().deleteAll()
        movieDbDataBase.movieDao().insert(movies)
    }

    override fun getMovie(movieId: Int): Single<Movie> {
        throw NotImplementedError("Not implemented. Call don`t have cache")
    }
}
