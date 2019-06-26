package com.gibranlyra.moviedbservice.db.configuration

import androidx.room.*
import com.gibranlyra.moviedbservice.model.Configuration

@Dao
interface ConfigurationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(configuration: Configuration): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(configurations: List<Configuration>): List<Long>

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun update(configuration: Configuration): Int

    @Delete
    fun delete(configuration: Configuration): Int

    @Query("SELECT * FROM configuration LIMIT 1")
    fun get(): List<Configuration>

    @Query("DELETE FROM configuration WHERE uId LIKE :id")
    fun deleteById(id: String)

    @Query("DELETE FROM configuration")
    fun deleteAll()
}