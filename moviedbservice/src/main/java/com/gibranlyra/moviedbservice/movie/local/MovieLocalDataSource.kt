package com.gibranlyra.moviedbservice.movie.local

import androidx.annotation.VisibleForTesting
import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.movie.MovieDataSource
import com.gibranlyra.moviedbservice.room.MovieDataBase
import io.reactivex.Single

class MovieLocalDataSource private constructor(private val movieDataBase: MovieDataBase) : MovieDataSource {

    companion object {
        private var INSTANCE: MovieLocalDataSource? = null

        @JvmStatic
        fun getInstance(movieDataBase: MovieDataBase): MovieLocalDataSource {
            if (INSTANCE == null) {
                synchronized(MovieLocalDataSource::javaClass) {
                    INSTANCE = MovieLocalDataSource(movieDataBase)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }

    override fun topRated(forceReload: Boolean, page: Int) = Single.fromCallable { movieDataBase.movieDao().getTopRated() }

    override fun upcoming(forceReload: Boolean, page: Int) = Single.fromCallable { movieDataBase.movieDao().getUpcoming() }

    override fun popular(forceReload: Boolean, page: Int) = Single.fromCallable { movieDataBase.movieDao().getPopular() }

    override fun saveMovies(movies: List<Movie>) {
        movieDataBase.movieDao().deleteAll()
        movieDataBase.movieDao().insert(movies)
    }

    override fun getMovie(movieId: String) = Single.fromCallable { movieDataBase.movieDao().findById(movieId) }
}