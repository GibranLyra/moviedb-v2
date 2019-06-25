package com.gibranlyra.moviedbservice.movie.remote

import com.gibranlyra.moviedbservice.MovieDbApiModule
import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.model.MovieDbResponse
import com.gibranlyra.moviedbservice.movie.MovieDataSource
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber

/* Vote Average constant for testing purposes */
internal const val VOTE_AVERAGE_DEFAULT: Int = 5

object MovieApi : MovieDataSource {
    private val movieService: MovieService by lazy { MovieDbApiModule.retrofit.create(MovieService::class.java) }

    override fun getMovies(forceReload: Boolean, page: Int): Single<List<Movie>> {
        return movieService.getMovies(page, VOTE_AVERAGE_DEFAULT)
                .map {
                    return@map it.results
                }
                .doOnError { Timber.e(it, "getMovies: %s", it.message) }
    }

    override fun getMovie(movieId: Int): Single<Movie> {
        return movieService.getMovie(movieId)
                .doOnError { e -> Timber.e(e, "getMovie: %s", e.message) }
    }

    internal interface MovieService {
        @GET("discover/movie")
        fun getMovies(@Query("page") page: Int,
                      @Query("vote_average.gte") voteAverage: Int): Single<MovieDbResponse>

        @GET("movie/{movieId}")
        fun getMovie(@Path("movieId") movieId: Int): Single<Movie>
    }
}