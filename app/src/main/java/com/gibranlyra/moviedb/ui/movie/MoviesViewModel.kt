package com.gibranlyra.moviedb.ui.movie

import android.app.Application
import androidx.lifecycle.MutableLiveData
import br.com.fastshop.ecommerce.refactor.util.schedulers.BaseSchedulerProvider
import com.gibranlyra.moviedb.ui.configuration.ConfigurationViewModel
import com.gibranlyra.moviedb.util.ext.buildImages
import com.gibranlyra.moviedb.util.resource.Resource
import com.gibranlyra.moviedbservice.configuration.ConfigurationDataSource
import com.gibranlyra.moviedbservice.model.Images
import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.movie.MovieDataSource
import timber.log.Timber

open class MoviesViewModel(application: Application,
                           configurationDataSource: ConfigurationDataSource,
                           private val movieDataSource: MovieDataSource,
                           scheduler: BaseSchedulerProvider)
    : ConfigurationViewModel(application, configurationDataSource, scheduler) {

    val topRatedLive = MutableLiveData<Resource<List<Movie>>>()
    val upcomingLive = MutableLiveData<Resource<List<Movie>>>()
    val popularLive = MutableLiveData<Resource<List<Movie>>>()

    fun start() {
        loadConfiguration()
    }

    open fun loadMovies(images: Images) {
        loadTopRated(images)
        loadUpcoming(images)
        loadPopular(images)
    }

    protected fun loadTopRated(images: Images) {
        subscriptions.add(movieDataSource.topRated()
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

    protected fun loadUpcoming(images: Images) {
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

    protected fun loadPopular(images: Images) {
        subscriptions.add(movieDataSource.popular()
                .subscribeOn(scheduler.io())
                .map { movies ->
                    movies.map { movie -> movie.buildImages(images) }
                }
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
}