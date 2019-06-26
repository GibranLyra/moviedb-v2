package com.gibranlyra.moviedb.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.fastshop.ecommerce.refactor.util.schedulers.BaseSchedulerProvider
import com.gibranlyra.moviedb.util.ext.buildImages
import com.gibranlyra.moviedb.util.resource.Resource
import com.gibranlyra.moviedbservice.configuration.ConfigurationDataSource
import com.gibranlyra.moviedbservice.model.Images
import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.movie.MovieDataSource
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MainViewModel(application: Application,
                    private val configurationDataSource: ConfigurationDataSource,
                    private val movieDataSource: MovieDataSource,
                    private val scheduler: BaseSchedulerProvider) : AndroidViewModel(application) {


    private val subscriptions: CompositeDisposable = CompositeDisposable()

    val configurationLive = MutableLiveData<Resource<Images>>()
    val topRatedLive = MutableLiveData<Resource<List<Movie>>>()
    val upcomingLive = MutableLiveData<Resource<List<Movie>>>()
    val popularLive = MutableLiveData<Resource<List<Movie>>>()

    fun start() {
        loadConfiguration()
    }

    fun loadMovies(images: Images) {
        loadTopRated(images)
        loadUpcoming(images)
        loadPopular(images)
    }

    private fun loadConfiguration() {
        subscriptions.add(configurationDataSource.getConfiguration(true)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .doOnSubscribe { configurationLive.value = Resource.loading(true) }
                .doFinally { configurationLive.value = Resource.loading(false) }
                .subscribe({
                    configurationLive.value = Resource.success(it.first().images!!)
                }, {
                    Timber.e(it, "loadConfiguration: %s", it.message)
                    val callback = { loadConfiguration() }
                    configurationLive.value = Resource.error(null, callback)
                }))
    }

    private fun loadTopRated(images: Images) {
        Timber.d("loadConfiguration: ")
        subscriptions.add(movieDataSource
                .topRated()
                .subscribeOn(scheduler.io())
                .map { it.map { movie -> movie.buildImages(images) } }
                .flatMap { updatedMovies ->
                    movieDataSource.update(updatedMovies)
                            .toSingle { }
                            .map { updatedMovies }
                }
                .observeOn(scheduler.ui())
                .doOnSubscribe { topRatedLive.value = Resource.loading(true) }
                .doFinally { topRatedLive.value = Resource.loading(false) }
                .subscribe({
                    topRatedLive.value = Resource.success(it)
                }, {
                    Timber.e(it, "loadTopRated: %s", it.message)
                    topRatedLive.value = Resource.error(null) { loadTopRated(images) }
                }))
    }

    private fun loadUpcoming(images: Images) {
        subscriptions.add(movieDataSource.upcoming()
                .subscribeOn(scheduler.io())
                .map { it.map { movie -> movie.buildImages(images) } }
                .flatMap { updatedMovies ->
                    movieDataSource.update(updatedMovies)
                            .toSingle { }
                            .map { updatedMovies }
                }
                .observeOn(scheduler.ui())
                .doOnSubscribe { upcomingLive.value = Resource.loading(true) }
                .doFinally { upcomingLive.value = Resource.loading(false) }
                .subscribe({
                    upcomingLive.value = Resource.success(it)
                }, {
                    Timber.e(it, "loadUpcoming: %s", it.message)
                    upcomingLive.value = Resource.error(null) { loadTopRated(images) }
                }))
    }

    private fun loadPopular(images: Images) {
        subscriptions.add(movieDataSource.popular()
                .subscribeOn(scheduler.io())
                .map { it.map { movie -> movie.buildImages(images) } }
                .flatMap { updatedMovies ->
                    movieDataSource.update(updatedMovies)
                            .toSingle { }
                            .map { updatedMovies }
                }
                .observeOn(scheduler.ui())
                .doOnSubscribe { popularLive.value = Resource.loading(true) }
                .doFinally { popularLive.value = Resource.loading(false) }
                .subscribe({
                    popularLive.value = Resource.success(it)
                }, {
                    Timber.e(it, "loadPopular: %s", it.message)
                    popularLive.value = Resource.error(null) { loadPopular(images) }
                }))
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}