package com.gibranlyra.moviedbservice.search

import com.gibranlyra.moviedbservice.model.Movie
import io.reactivex.Single

interface SearchDataSource {
    fun search(query: String, page: Int = 1): Single<List<Movie>>
}