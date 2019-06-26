package com.gibranlyra.moviedbservice.movie.local

import androidx.annotation.VisibleForTesting
import com.gibranlyra.moviedbservice.db.movie.MovieDataBase
import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.movie.MovieDataSource
import io.reactivex.Completable
import timber.log.Timber

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

    override fun topRated(forceReload: Boolean, page: Int) = movieDataBase.movieDao().getTopRated()

    override fun upcoming(forceReload: Boolean, page: Int) = movieDataBase.movieDao().getUpcoming()

    override fun popular(forceReload: Boolean, page: Int) = movieDataBase.movieDao().getPopular()

    override fun saveMovies(movies: List<Movie>) = movieDataBase.movieDao()
            .deleteAll()
            .toSingle {  }
            .flatMapCompletable { movieDataBase.movieDao().insert(movies) }


    override fun update(movies: List<Movie>): Completable = movieDataBase.movieDao()
            .update(movies)
            .toSingle {  }
            .flatMap {
                movieDataBase.movieDao().getAll()
            }.map {
              Timber.d("update: ")
            }.ignoreElement()

    override fun update(movie: Movie) = movieDataBase.movieDao().update(movie)

    override fun getMovie(movieId: String) = movieDataBase.movieDao().findById(movieId)
}