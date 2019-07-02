package com.gibranlyra.moviedb.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import br.com.fastshop.ecommerce.refactor.util.schedulers.BaseSchedulerProvider
import com.gibranlyra.moviedb.ui.configuration.ConfigurationViewModel
import com.gibranlyra.moviedb.util.ext.buildImages
import com.gibranlyra.moviedb.util.resource.Resource
import com.gibranlyra.moviedbservice.configuration.ConfigurationDataSource
import com.gibranlyra.moviedbservice.model.Images
import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.search.SearchDataSource
import timber.log.Timber

class SearchViewModel(application: Application,
                      configurationDataSource: ConfigurationDataSource,
                      private val searchDataSource: SearchDataSource,
                      scheduler: BaseSchedulerProvider)
    : ConfigurationViewModel(application, configurationDataSource, scheduler) {

    val searchLive = MutableLiveData<Resource<List<Movie>>>()

    lateinit var images: Images

    internal fun start() {
        loadConfiguration()
    }

    internal fun loadMovies(query: String, images: Images? = null) {
        images?.let {
            this.images = it
        }
        loadSearch(query)
    }

    internal fun loadSearch(query: String) {
        subscriptions.add(searchDataSource.search(1, query)
                .subscribeOn(scheduler.io())
                .map { it.map { movie -> movie.buildImages(images) } }
                .observeOn(scheduler.ui())
                .doOnSubscribe { searchLive.value = Resource.loading(true) }
                .doFinally { searchLive.value = Resource.loading(false) }
                .subscribe({
                    searchLive.value = Resource.success(it)
                }, {
                    Timber.e(it, "loadSearch: %s", it.message)
                    searchLive.value = Resource.error(null) { loadSearch(query) }
                }))
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}