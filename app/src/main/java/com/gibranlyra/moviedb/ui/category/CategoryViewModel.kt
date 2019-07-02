package com.gibranlyra.moviedb.ui.category

import android.app.Application
import br.com.fastshop.ecommerce.refactor.util.schedulers.BaseSchedulerProvider
import com.gibranlyra.moviedb.ui.category.CategoryViewModel.Category.*
import com.gibranlyra.moviedb.ui.movie.MoviesViewModel
import com.gibranlyra.moviedbservice.configuration.ConfigurationDataSource
import com.gibranlyra.moviedbservice.model.Images
import com.gibranlyra.moviedbservice.movie.MovieDataSource

class CategoryViewModel(application: Application,
                        configurationDataSource: ConfigurationDataSource,
                        movieDataSource: MovieDataSource,
                        scheduler: BaseSchedulerProvider)
    : MoviesViewModel(application, configurationDataSource, movieDataSource, scheduler) {

    fun loadCategory(images: Images, category: Category) {
        when (category) {
            TOP_RATED -> loadTopRated(images)
            UPCOMING -> loadUpcoming(images)
            POPULAR -> loadPopular(images)
        }
    }

    enum class Category {
        TOP_RATED,
        UPCOMING,
        POPULAR
    }
}