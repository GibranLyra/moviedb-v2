package com.gibranlyra.moviedbservice.db.movie

import androidx.room.*
import com.gibranlyra.moviedbservice.model.Movie
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<Movie>): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(movie: Movie): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(movie: List<Movie>): Completable

    @Delete
    fun delete(movie: Movie): Completable

    @Query("SELECT * FROM movie")
    fun getAll(): Single<List<Movie>>

    @Query("SELECT * FROM movie WHERE id LIKE :id")
    fun findById(id: Int): Single<Movie>

    @Query("DELETE FROM movie WHERE id LIKE :id")
    fun deleteById(id: String): Completable

    @Query("DELETE FROM movie")
    fun deleteAll(): Completable

    @Query("SELECT * FROM movie ORDER BY voteAverage DESC LIMIT 20")
    fun getTopRated(): Single<List<Movie>>

    @Query("SELECT * FROM movie ORDER BY popularity DESC LIMIT 20")
    fun getPopular(): Single<List<Movie>>

    @Query("SELECT * FROM movie ORDER BY releaseDate DESC LIMIT 20")
    fun getUpcoming(): Single<List<Movie>>
}