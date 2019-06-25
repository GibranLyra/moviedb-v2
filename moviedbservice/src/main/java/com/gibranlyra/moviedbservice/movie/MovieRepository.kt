package com.gibranlyra.moviedbservice.movie

import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.movie.local.MovieLocalDataSource
import io.reactivex.Single

object MovieRepository : MovieDataSource {
    lateinit var remoteDataSource: MovieDataSource

    lateinit var localDataSource: MovieLocalDataSource
    var initialized = false

    fun init(remoteDataSource: MovieDataSource, localDataSource: MovieLocalDataSource) {
        this.remoteDataSource = remoteDataSource
        this.localDataSource = localDataSource
        initialized = true
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
    override fun getMovie(movieId: Int): Single<Movie> {
        return remoteDataSource.getMovie(movieId)
    }
}
