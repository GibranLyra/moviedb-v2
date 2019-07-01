package com.gibranlyra.moviedb.ui.moviedetail

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

class MovieDetailViewModel(application: Application,
                           configurationDataSource: ConfigurationDataSource,
                           private val movieDataSource: MovieDataSource,
                           scheduler: BaseSchedulerProvider)
    : ConfigurationViewModel(application, configurationDataSource, scheduler) {

    val movieDetailsLive = MutableLiveData<Resource<Movie>>()

    fun start() {
        loadConfiguration()
    }

    internal fun loadMovieDetails(movieId: Int, images: Images) {
        subscriptions.add(movieDataSource.getMovie(movieId)
                .subscribeOn(scheduler.io())
                .map { movie -> movie.buildImages(images) }
                .observeOn(scheduler.ui())
                .doOnSubscribe { movieDetailsLive.value = Resource.loading(true) }
                .doFinally { movieDetailsLive.value = Resource.loading(false) }
                .subscribe({ movieDetailsLive.value = Resource.success(it) }, {
                    Timber.e(it, "loadMovieDetails: %s", it.message)
                    val callback = { loadMovieDetails(movieId, images) }
                    movieDetailsLive.value = Resource.error("Erro genérico", callback)
                }))
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}