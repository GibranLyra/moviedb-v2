package com.gibranlyra.moviedb.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.fastshop.ecommerce.refactor.util.schedulers.BaseSchedulerProvider
import com.gibranlyra.moviedb.util.resource.Resource
import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.movie.MovieDataSource
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MainViewModel(application: Application,
                    private val movieDataSource: MovieDataSource,
                    private val scheduler: BaseSchedulerProvider) : AndroidViewModel(application) {

    private val subscriptions: CompositeDisposable = CompositeDisposable()

    val movieLive = MutableLiveData<Resource<List<Movie>>>()

    fun loadMovies() {
        subscriptions.add(movieDataSource.getMovies()
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .doOnSubscribe { movieLive.value = Resource.loading(true) }
                .doFinally { movieLive.value = Resource.loading(false) }
                .subscribe({
                    movieLive.value = Resource.success(it)
                }, {
                    Timber.e(it, "loadMovies: %s", it.message)
                    movieLive.value = Resource.error("Erro genérico") { loadMovies() }
                }))
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}