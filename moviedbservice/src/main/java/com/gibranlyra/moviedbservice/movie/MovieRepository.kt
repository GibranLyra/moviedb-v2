package com.gibranlyra.moviedbservice.movie

import com.gibranlyra.moviedbservice.model.Movie
import io.reactivex.Completable
import io.reactivex.Single

class MovieRepository(private val remoteDataSource: MovieDataSource,
                      private val localDataSource: MovieDataSource) : MovieDataSource {
    companion object {

        private var INSTANCE: MovieRepository? = null
        @JvmStatic
        fun getInstance(movieRemoteDataSource: MovieDataSource, movieLocalDataSource: MovieDataSource) =
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

    override fun topRated(forceReload: Boolean, page: Int): Single<List<Movie>> {
        cacheIsDirty = forceReload
        if (cachedMovies != null && !cacheIsDirty) {
            return Single.just(cachedMovies)
        }
        return if (cacheIsDirty) {
            remoteDataSource
                    .topRated()
                    .flatMap { localDataSource.saveMovies(it).toSingle { it } }
                    .doOnSuccess { cacheIsDirty = false }
        } else {
            localDataSource.topRated()
                    .flatMap {
                        when {
                            it.isEmpty() -> {
                                remoteDataSource.topRated()
                                        .flatMap { movies -> localDataSource.saveMovies(movies).toSingle { movies } }
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

    override fun upcoming(forceReload: Boolean, page: Int): Single<List<Movie>> {
        cacheIsDirty = forceReload
        if (cachedMovies != null && !cacheIsDirty) {
            return Single.just(cachedMovies)
        }
        return if (cacheIsDirty) {
            remoteDataSource
                    .upcoming()
                    .flatMap { localDataSource.saveMovies(it).toSingle { it } }
                    .doOnSuccess {
                        localDataSource.saveMovies(it)
                        cacheIsDirty = false
                    }
        } else {
            localDataSource.upcoming()
                    .flatMap {
                        when {
                            it.isEmpty() -> {
                                remoteDataSource.upcoming()
                                        .flatMap { movies -> localDataSource.saveMovies(movies).toSingle { movies } }
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

    override fun popular(forceReload: Boolean, page: Int): Single<List<Movie>> {
        cacheIsDirty = forceReload
        if (cachedMovies != null && !cacheIsDirty) {
            return Single.just(cachedMovies)
        }
        return if (cacheIsDirty) {
            remoteDataSource
                    .popular()
                    .flatMap { localDataSource.saveMovies(it).toSingle { it } }
                    .doOnSuccess { cacheIsDirty = false }
        } else {
            localDataSource.popular()
                    .flatMap {
                        when {
                            it.isEmpty() -> {
                                remoteDataSource.popular()
                                        .flatMap { movies -> localDataSource.saveMovies(movies).toSingle { movies } }
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

    override fun update(movie: Movie): Completable {
        return localDataSource.update(movie)
    }

    override fun update(movies: List<Movie>): Completable {
        return localDataSource.update(movies)
    }
}
