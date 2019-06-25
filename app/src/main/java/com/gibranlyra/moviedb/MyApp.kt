package com.gibranlyra.moviedb

import android.app.Application
import com.gibranlyra.moviedbservice.MovieDbApiModule
import timber.log.Timber

class MyApp : Application() {

    companion object {
        lateinit var instance: MyApp private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeTimber()
        initializeApiModules()
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initializeApiModules() {
        //Initialize ApiModule Singleton
        MovieDbApiModule.buildRetrofit()
    }

}