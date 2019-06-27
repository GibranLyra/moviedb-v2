package com.gibranlyra.moviedbservice.db.configuration

import androidx.room.*
import com.gibranlyra.moviedbservice.model.Configuration
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ConfigurationDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(configuration: Configuration): Completable

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(configurations: List<Configuration>): Completable

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun update(configuration: Configuration): Completable

    @Delete
    fun delete(configuration: Configuration): Completable

    @Query("SELECT * FROM configuration LIMIT 1")
    fun get(): Single<List<Configuration>>

    @Query("DELETE FROM configuration WHERE uId LIKE :id")
    fun deleteById(id: String): Completable

    @Query("DELETE FROM configuration")
    fun deleteAll(): Completable
}