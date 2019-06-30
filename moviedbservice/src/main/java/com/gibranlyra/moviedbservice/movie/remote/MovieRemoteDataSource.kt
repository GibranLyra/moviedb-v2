package com.gibranlyra.moviedbservice.movie.remote

import com.gibranlyra.moviedbservice.MovieDbApiModule
import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.model.MovieDbResponse
import com.gibranlyra.moviedbservice.movie.MovieDataSource
import com.gibranlyra.moviedbservice.movie.remote.SortBy.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber


const val MOVIEDB_LANGUAGE = "en-US"

enum class SortBy(val serializeName: String) {
    POPULARITY("popularity.desc"),
    RELEASE_DATE("release_date.asc"),
    TOP_RATED("top_rated.desc")
}

object MovieRemoteDataSource : MovieDataSource {
    private val movieService: MovieService by lazy { MovieDbApiModule.retrofit.create(MovieService::class.java) }

    override fun getMovie(movieId: Int): Single<Movie> {
        return movieService.getMovie(movieId)
                .doOnError { e -> Timber.e(e, "getMovie: %s", e.message) }
    }

    override fun topRated(forceReload: Boolean, page: Int): Single<List<Movie>> {
        return movieService.discoverMovies(page, MOVIEDB_LANGUAGE, TOP_RATED.serializeName)
                .map { return@map it.results }
                .doOnError { Timber.e(it, "topRated: ${it.message}") }
    }

    override fun upcoming(forceReload: Boolean, page: Int): Single<List<Movie>> {
        return movieService.discoverMovies(page, MOVIEDB_LANGUAGE, RELEASE_DATE.serializeName)
                .map { return@map it.results }
                .doOnError { Timber.e(it, "upcoming: ${it.message}") }

    }

    override fun popular(forceReload: Boolean, page: Int): Single<List<Movie>> {
        return movieService.discoverMovies(page,  MOVIEDB_LANGUAGE,POPULARITY.serializeName)
                .map { return@map it.results }
                .doOnError { Timber.e(it, "popular: ${it.message}") }

    }

    internal interface MovieService {

        @GET("movie/{movieId}")
        fun getMovie(@Path("movieId") movieId: String): Single<Movie>

        @GET("discover/movie")
        fun discoverMovies(@Query("page") page: Int,
                           @Query("language") language: String,
                           @Query("sort_by") sortBy: String): Single<MovieDbResponse<Movie>>
    }
}