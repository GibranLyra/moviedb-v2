package com.gibranlyra.moviedbservice.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gibranlyra.moviedbservice.model.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDbDataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}