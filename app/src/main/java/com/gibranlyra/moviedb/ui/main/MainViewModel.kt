package com.gibranlyra.moviedb.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.fastshop.ecommerce.refactor.util.schedulers.BaseSchedulerProvider
import com.gibranlyra.moviedb.util.resource.Resource
import com.gibranlyra.moviedbservice.configuration.ConfigurationDataSource
import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.movie.MovieDataSource
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MainViewModel(application: Application,
                    private val configurationDataSource: ConfigurationDataSource,
                    private val movieDataSource: MovieDataSource,
                    private val scheduler: BaseSchedulerProvider) : AndroidViewModel(application) {


    private val subscriptions: CompositeDisposable = CompositeDisposable()

    val configurationLive = MutableLiveData<Resource<Unit>>()
    val topRatedLive = MutableLiveData<Resource<List<Movie>>>()
    val upcomingLive = MutableLiveData<Resource<List<Movie>>>()
    val popularLive = MutableLiveData<Resource<List<Movie>>>()

    fun start() {
        loadConfiguration()
    }

    fun loadMovies() {
        loadTopRated()
        loadUpcoming()
        loadPopular()
    }

    private fun loadConfiguration() {
        subscriptions.add(configurationDataSource.getConfiguration()
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .doOnSubscribe { configurationLive.value = Resource.loading(true) }
                .doFinally { configurationLive.value = Resource.loading(false) }
                .subscribe({
                    configurationLive.value = Resource.success(Unit)
                }, {
                    Timber.e(it, "loadConfiguration: %s", it.message)
                    val callback = { loadConfiguration() }
                    configurationLive.value = Resource.error(null, callback)
                }))
    }

    private fun loadTopRated() {
        subscriptions.add(movieDataSource.topRated(true)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .doOnSubscribe { topRatedLive.value = Resource.loading(true) }
                .doFinally { topRatedLive.value = Resource.loading(false) }
                .subscribe({
                    topRatedLive.value = Resource.success(it)
                }, {
                    Timber.e(it, "loadTopRated: %s", it.message)
                    val callback = { loadTopRated() }
                    topRatedLive.value = Resource.error(null, callback)
                }))
    }

    private fun loadUpcoming() {
        subscriptions.add(movieDataSource.upcoming(true)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .doOnSubscribe { upcomingLive.value = Resource.loading(true) }
                .doFinally { upcomingLive.value = Resource.loading(false) }
                .subscribe({
                    upcomingLive.value = Resource.success(it)
                }, {
                    Timber.e(it, "loadUpcoming: %s", it.message)
                    val callback = { loadTopRated() }
                    upcomingLive.value = Resource.error(null, callback)
                }))
    }

    private fun loadPopular() {
        subscriptions.add(movieDataSource.popular(true)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .doOnSubscribe { popularLive.value = Resource.loading(true) }
                .doFinally { popularLive.value = Resource.loading(false) }
                .subscribe({
                    popularLive.value = Resource.success(it)
                }, {
                    Timber.e(it, "loadPopular: %s", it.message)
                    val callback = { loadPopular() }
                    popularLive.value = Resource.error(null, callback)
                }))
    }


    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}