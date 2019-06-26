package com.gibranlyra.moviedbservice.movie

import com.gibranlyra.moviedbservice.model.Movie
import io.reactivex.Single

interface MovieDataSource {
    fun discoverMovies(forceReload: Boolean = false, page: Int = 1): Single<List<Movie>>
    fun getMovie(movieId: String): Single<Movie>
    fun topRated(forceReload: Boolean = false, page: Int = 1): Single<List<Movie>>
    fun saveMovies(movies: List<Movie>) {}
}