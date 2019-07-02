package com.gibranlyra.moviedbservice.search

import com.gibranlyra.moviedbservice.model.Movie
import io.reactivex.Single

interface SearchDataSource {
    fun search(page: Int = 1, query: String): Single<List<Movie>>
}