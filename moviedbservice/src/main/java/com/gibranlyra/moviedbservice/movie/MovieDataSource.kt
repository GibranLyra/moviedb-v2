package com.gibranlyra.moviedbservice.movie

import com.gibranlyra.moviedbservice.model.Movie
import io.reactivex.Single

interface MovieDataSource {
    fun getMovies(forceReload: Boolean = false, page: Int = 1): Single<List<Movie>>
    fun getMovie(movieId: String): Single<Movie>
    fun saveMovies(movies: List<Movie>) {}
}