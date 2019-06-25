package com.gibranlyra.moviedbservice.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gibranlyra.moviedbservice.model.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        private var INSTANCE: MovieDataBase? = null

        private val lock = Any()

        fun getInstance(context: Context): MovieDataBase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, MovieDataBase::class.java, "Movies.db")
                            .build()
                }
                return INSTANCE!!
            }
        }
    }
}