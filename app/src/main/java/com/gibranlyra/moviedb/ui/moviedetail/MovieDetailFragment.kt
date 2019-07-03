package com.gibranlyra.moviedb.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gibranlyra.moviedb.MyApp
import com.gibranlyra.moviedb.R
import com.gibranlyra.moviedb.di.ViewModelFactory
import com.gibranlyra.moviedb.ui.base.BaseBottomSheetDialog
import com.gibranlyra.moviedb.util.DateUtil
import com.gibranlyra.moviedb.util.ext.loadImage
import com.gibranlyra.moviedb.util.ext.requiredBundleNotFound
import com.gibranlyra.moviedb.util.ext.showSnackBar
import com.gibranlyra.moviedb.util.resource.ResourceState.*
import com.gibranlyra.moviedbservice.model.Movie
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movie_detail.*

const val EXTRA_MOVIE = "EXTRA_MOVIE"

class MovieDetailFragment : BaseBottomSheetDialog() {
    companion object {
        @JvmStatic
        fun newInstance(movie: Movie): MovieDetailFragment {
            val fragment = MovieDetailFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(EXTRA_MOVIE, movie)
            }
            return fragment
        }
    }

    private val viewModel: MovieDetailViewModel by lazy {
        ViewModelProviders
                .of(this, ViewModelFactory.getInstance(MyApp.instance))
                .get(MovieDetailViewModel::class.java)
    }

    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<Movie>(EXTRA_MOVIE)?.let {
            movie = it
        } ?: run {
            activity?.requiredBundleNotFound(EXTRA_MOVIE)
        }
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie_detail, container, false)
        view.findViewById<Toolbar>(R.id.fragmentMovieDetailToolbar).run {
            setNavigationIcon(R.drawable.ic_arrow_downward)
            setNavigationOnClickListener { dismiss() }

            navigationContentDescription = "Back Button"
            val outViews = arrayListOf<View>()
            findViewsWithText(outViews, "Back Button", View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
            backButton = outViews.first()

            title = movie.title
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showInitialMovieInfo()
    }

    fun initViewModel() {
        with(viewModel) {
            configurationLive.observe(this@MovieDetailFragment, Observer {
                when (it.status) {
                    LOADING -> when (it.loading) { /*DO NOTHING*/
                    }
                    SUCCESS -> loadMovieDetails(movie.id, it.data!!)
                    ERROR -> showError(it.message!!, it.action!!)
                }
            })

            movieDetailsLive.observe(this@MovieDetailFragment, Observer {
                when (it.status) {
                    LOADING -> {
                        when (it.loading) {
                            true -> movieDetailLoadingView.show()
                            false -> movieDetailLoadingView.hide()
                        }
                    }

                    SUCCESS -> {
                        movie = it.data!!
                        showDetailedInfo()
                    }
                    ERROR -> showError(it.message!!, it.action!!)
                }
            })

            start()
        }
    }

    private fun showInitialMovieInfo() {
        with(movie) {
            movieDetailMovieImageView.loadImage(posterPath!!)
            movieDetailMovieTitleView.text = originalTitle
            movieDetailReleaseDateView.text = releaseDate?.let { DateUtil.convertToFriendlyDate(it) }
            movieDetailRatingView.rating = movie.voteAverage?.toFloat()?.div(2) ?: 0f
        }
    }

    private fun showDetailedInfo() {
        with(movie) {
            movieDetailLengthView.text = context?.getString(R.string.movie_length, runtime)
            movieDetailLanguageView.text = originalLanguage
            movieDetailReleaseStatusView.text = status
            movieDetailLanguageView.text = originalLanguage
            movieDetailHomePageView.text = homepage
            movieDetailDescription.text = overview
        }
    }

    private fun showError(message: String, action: () -> Unit) {
        movieDetailRootView.showSnackBar(message, Snackbar.LENGTH_LONG, getString(R.string.try_again), action)
    }
}