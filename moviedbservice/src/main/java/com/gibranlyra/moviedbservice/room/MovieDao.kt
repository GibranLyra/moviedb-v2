package com.gibranlyra.moviedbservice.room

import androidx.room.*
import com.gibranlyra.moviedbservice.model.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(movies: List<Movie>): List<Long>

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun update(movie: Movie): Int

    @Delete
    fun delete(movie: Movie): Int

    @Query("SELECT * FROM movie")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movie WHERE id LIKE :id")
    fun findById(id: String): Movie

    @Query("DELETE FROM movie WHERE id LIKE :id")
    fun deleteById(id: String)

    @Query("DELETE FROM movie")
    fun deleteAll()
}