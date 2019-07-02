package com.gibranlyra.moviedbservice.search

import com.gibranlyra.moviedbservice.model.Movie
import io.reactivex.Single
import retrofit2.HttpException
import java.net.UnknownHostException

class SearchRepository(private val remoteDataSource: SearchDataSource,
                       private val localDataSource: SearchDataSource) : SearchDataSource {
    companion object {

        private var INSTANCE: SearchRepository? = null
        @JvmStatic
        fun getInstance(searchRemoteDataSource: SearchDataSource, searchLocalDataSource: SearchDataSource) =
                INSTANCE ?: synchronized(SearchRepository::class.java) {
                    INSTANCE ?: SearchRepository(searchRemoteDataSource, searchLocalDataSource)
                            .also { INSTANCE = it }
                }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }

    private var cachedMovies: List<Movie>? = null
    private var lastQuery: String? = null

    private var cacheIsDirty = false

    override fun search(query: String, page: Int): Single<List<Movie>> {
        lastQuery?.let { lastQuery ->
            if (cachedMovies != null && !cacheIsDirty && lastQuery.equals(query, true)) {
                return Single.just(cachedMovies)
            }
        } ?: run {
            cacheIsDirty = true
        }
        return if (cacheIsDirty) {
            remoteDataSource
                    .search(query)
                    .onErrorResumeNext {
                        if (it is HttpException || it is UnknownHostException) {
                            localDataSource.search(query)
                                    .map { movies ->
                                        cachedMovies = movies
                                        movies
                                    }
                        } else {
                            Single.error(it)
                        }
                    }
                    .doOnSuccess { cacheIsDirty = true }
        } else {
            localDataSource.search(query)
                    .map {
                        cachedMovies = it
                        return@map it
                    }
        }
    }
}
