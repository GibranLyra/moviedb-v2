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

object MovieRemoteDataSource : MovieDataSource {
    private val movieService: MovieService by lazy { MovieDbApiModule.retrofit.create(MovieService::class.java) }

    override fun discoverMovies(forceReload: Boolean, page: Int): Single<List<Movie>> {
        return movieService.discoverMovies(page, VOTE_AVERAGE_DEFAULT)
                .map {
                    return@map it.results
                }
                .doOnError { Timber.e(it, "discoverMovies: %s", it.message) }
    }

    override fun getMovie(movieId: String): Single<Movie> {
        return movieService.getMovie(movieId)
                .doOnError { e -> Timber.e(e, "getMovie: %s", e.message) }
    }

    override fun topRated(forceReload: Boolean, page: Int): Single<List<Movie>> {
        return movieService.topRated(page)
                .map { return@map it.results }
                .doOnError { Timber.e(it, "topRated: ${it.message}") }
    }

    override fun upcoming(forceReload: Boolean, page: Int): Single<List<Movie>> {
        return movieService.upcoming(page)
                .map { return@map it.results }
                .doOnError { Timber.e(it, "upcoming: ${it.message}") }

    }

    override fun popular(forceReload: Boolean, page: Int): Single<List<Movie>> {
        return movieService.popular(page)
                .map { return@map it.results }
                .doOnError { Timber.e(it, "popular: ${it.message}") }

    }

    internal interface MovieService {
        @GET("discover/movie")
        fun discoverMovies(@Query("page") page: Int,
                           @Query("vote_average.gte") voteAverage: Int): Single<MovieDbResponse<Movie>>

        @GET("movie/{movieId}")
        fun getMovie(@Path("movieId") movieId: String): Single<Movie>

        @GET("movie/topRated")
        fun topRated(@Query("page") page: Int): Single<MovieDbResponse<Movie>>

        @GET("movie/upcoming")
        fun upcoming(@Query("page") page: Int): Single<MovieDbResponse<Movie>>

        @GET("movie/popular")
        fun popular(@Query("page") page: Int): Single<MovieDbResponse<Movie>>
    }
}