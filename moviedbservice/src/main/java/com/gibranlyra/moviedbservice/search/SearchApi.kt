package com.gibranlyra.moviedbservice.search

import com.gibranlyra.moviedbservice.MovieDbApiModule
import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.movie.SearchDataSource
import io.reactivex.Single
import timber.log.Timber

object SearchApi : SearchDataSource {

    private val searchService: SearchService by lazy { MovieDbApiModule.retrofit.create(SearchService::class.java) }

    override fun search(page: Int, query: String): Single<List<Movie>> {
        return searchService.getMovies(page, query)
                .map {
                    return@map it.results
                }
                .doOnError { e -> Timber.e(e, "search: %s", e.message) }
    }
}