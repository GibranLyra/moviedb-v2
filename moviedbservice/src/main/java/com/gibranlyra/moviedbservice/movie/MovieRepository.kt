package com.gibranlyra.moviedbservice.movie

import com.gibranlyra.moviedbservice.model.Movie
import io.reactivex.Single

class MovieRepository(private val remoteDataSource: MovieDataSource,
                      private val localDataSource: MovieDataSource) : MovieDataSource {

    companion object {

        private var INSTANCE: MovieRepository? = null
        @JvmStatic
        fun getInstance(movieRemoteDataSource: MovieDataSource,
                        movieLocalDataSource: MovieDataSource) =
                INSTANCE ?: synchronized(MovieRepository::class.java) {
                    INSTANCE ?: MovieRepository(movieRemoteDataSource, movieLocalDataSource)
                            .also { INSTANCE = it }
                }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }

    private var cachedMovies: List<Movie>? = null

    private var cacheIsDirty = false

    override fun getMovies(forceReload: Boolean, page: Int): Single<List<Movie>> {
        cacheIsDirty = forceReload
        // Respond immediately with cache if available and not dirty
        if (cachedMovies != null && !cacheIsDirty) {
            return Single.just(cachedMovies)
        }
        return if (cacheIsDirty) {
            remoteDataSource
                    .getMovies()
                    .doOnSuccess {
                        localDataSource.saveMovies(it)
                        cacheIsDirty = false
                    }
        } else {
            //Check local data source, if is empty call the remote
            localDataSource.getMovies()
                    .flatMap {
                        when {
                            it.isEmpty() -> {
                                remoteDataSource.getMovies()
                                        .map { movies ->
                                            localDataSource.saveMovies(movies)
                                            return@map movies
                                        }
                            }
                            else -> {
                                Single.just(it)
                            }
                        }
                    }
                    .map {
                        cachedMovies = it
                        return@map it
                    }
        }
    }

    /*not cached*/
    override fun getMovie(movieId: String): Single<Movie> {
        return remoteDataSource.getMovie(movieId)
    }
}
