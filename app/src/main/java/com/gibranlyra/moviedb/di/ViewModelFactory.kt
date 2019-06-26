package com.gibranlyra.moviedb.di

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fastshop.ecommerce.refactor.util.schedulers.SchedulerProvider
import com.gibranlyra.moviedb.MyApp
import com.gibranlyra.moviedb.ui.main.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(private val application: MyApp) :
        ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(MainViewModel::class.java) ->
                        MainViewModel(application,
                                Injection.provideConfigurationRepository(application),
                                Injection.provideMovieRepository(application), SchedulerProvider)

                    else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) = INSTANCE
                ?: synchronized(ViewModelFactory::class.java) {
                    val viewModelFactory = INSTANCE ?: ViewModelFactory(application as MyApp)
                            .also { INSTANCE = it }
                    viewModelFactory
                }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
