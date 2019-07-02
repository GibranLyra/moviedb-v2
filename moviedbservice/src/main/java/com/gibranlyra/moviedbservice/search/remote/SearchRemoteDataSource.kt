package com.gibranlyra.moviedbservice.search.remote

import com.gibranlyra.moviedbservice.MovieDbApiModule
import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.model.MovieDbResponse
import com.gibranlyra.moviedbservice.search.SearchDataSource
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber

object SearchRemoteDataSource : SearchDataSource {

    private val searchService: SearchService by lazy { MovieDbApiModule.retrofit.create(SearchRemoteDataSource.SearchService::class.java) }

    override fun search(query: String, page: Int): Single<List<Movie>> {
        return searchService.getMovies(page, query)
                .map {
                    return@map it.results
                }
                .doOnError { e -> Timber.e(e, "search: %s", e.message) }
    }

    internal interface SearchService {
        @GET("search/movie")
        fun getMovies(@Query("page") page: Int,
                      @Query("query") query: String): Single<MovieDbResponse<Movie>>
    }
}