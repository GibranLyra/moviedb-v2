package com.gibranlyra.moviedb.ui.configuration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.fastshop.ecommerce.refactor.util.schedulers.BaseSchedulerProvider
import com.gibranlyra.moviedb.util.resource.Resource
import com.gibranlyra.moviedbservice.configuration.ConfigurationDataSource
import com.gibranlyra.moviedbservice.model.Images
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class ConfigurationViewModel(application: Application,
                             val configurationDataSource: ConfigurationDataSource,
                             val scheduler: BaseSchedulerProvider) : AndroidViewModel(application) {

    internal val subscriptions: CompositeDisposable = CompositeDisposable()

    internal val configurationLive = MutableLiveData<Resource<Images>>()

    internal fun loadConfiguration() {
        subscriptions.add(configurationDataSource.getConfiguration()
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .doOnSubscribe { configurationLive.value = Resource.loading(true) }
                .doFinally { configurationLive.value = Resource.loading(false) }
                .subscribe({
                    configurationLive.value = Resource.success(it.first().images!!)
                }, {
                    Timber.e(it, "loadConfiguration: %s", it.message)
                    val callback = { loadConfiguration() }
                    configurationLive.value = Resource.error("Erro genérico", callback)
                }))
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}