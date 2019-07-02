package com.gibranlyra.moviedbservice.search.local

import androidx.annotation.VisibleForTesting
import com.gibranlyra.moviedbservice.db.movie.MovieDataBase
import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.search.SearchDataSource
import io.reactivex.Single

class SearchLocalDataSource private constructor(private val movieDataBase: MovieDataBase) : SearchDataSource {

    companion object {
        private var INSTANCE: SearchLocalDataSource? = null

        @JvmStatic
        fun getInstance(searchDataBase: MovieDataBase): SearchLocalDataSource {
            if (INSTANCE == null) {
                synchronized(SearchLocalDataSource::javaClass) {
                    INSTANCE = SearchLocalDataSource(searchDataBase)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }

    override fun search(query: String, page: Int): Single<List<Movie>> {
        return movieDataBase.movieDao().findByTitle(query)
    }

}