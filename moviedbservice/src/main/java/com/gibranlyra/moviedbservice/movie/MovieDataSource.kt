package com.gibranlyra.moviedbservice.movie

import com.gibranlyra.moviedbservice.model.Movie
import io.reactivex.Completable
import io.reactivex.Single

interface MovieDataSource {
    fun getMovie(movieId: String): Single<Movie>
    fun topRated(forceReload: Boolean = false, page: Int = 1): Single<List<Movie>>
    fun upcoming(forceReload: Boolean = false, page: Int = 1): Single<List<Movie>>
    fun popular(forceReload: Boolean = false, page: Int = 1): Single<List<Movie>>
    fun saveMovies(movies: List<Movie>): Completable = Completable.complete()
    fun update(movie: Movie): Completable = Completable.complete()
    fun update(movies: List<Movie>): Completable = Completable.complete()
}