package com.gibranlyra.moviedb.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gibranlyra.moviedb.MyApp
import com.gibranlyra.moviedb.R
import com.gibranlyra.moviedb.di.ViewModelFactory
import com.gibranlyra.moviedb.ui.category.CategoryFragment
import com.gibranlyra.moviedb.ui.category.CategoryViewModel
import com.gibranlyra.moviedb.ui.component.BaseAdapter
import com.gibranlyra.moviedb.ui.component.error.ErrorView
import com.gibranlyra.moviedb.ui.component.movie.HorizontalMovieAdapter
import com.gibranlyra.moviedb.ui.moviedetail.MovieDetailFragment
import com.gibranlyra.moviedb.ui.search.SearchFragment
import com.gibranlyra.moviedb.util.ext.gone
import com.gibranlyra.moviedb.util.ext.replaceFragment
import com.gibranlyra.moviedb.util.ext.showSnackBar
import com.gibranlyra.moviedb.util.ext.visible
import com.gibranlyra.moviedb.util.resource.ResourceState.*
import com.gibranlyra.moviedbservice.model.Movie
import com.google.android.material.snackbar.Snackbar
import com.lapism.searchview.Search
import kotlinx.android.synthetic.main.fragment_movies.*

class MoviesFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(): MoviesFragment {
            return MoviesFragment()
        }
    }

    private val viewModel: MoviesViewModel by lazy {
        ViewModelProviders
                .of(this, ViewModelFactory.getInstance(MyApp.instance))
                .get(MoviesViewModel::class.java)
    }

    private val topRatedAdapter by lazy {
        HorizontalMovieAdapter(mutableListOf(), object : BaseAdapter.AdapterListener<Movie> {
            override fun onAdapterItemClicked(position: Int, item: Movie, view: View) {
                MovieDetailFragment.newInstance(item)
                        .show(childFragmentManager, tag)
            }
        })
    }

    private val upcomingAdapter by lazy {
        HorizontalMovieAdapter(mutableListOf(), object : BaseAdapter.AdapterListener<Movie> {
            override fun onAdapterItemClicked(position: Int, item: Movie, view: View) {
                MovieDetailFragment.newInstance(item)
                        .show(childFragmentManager, tag)
            }
        })
    }

    private val popularAdapter by lazy {
        HorizontalMovieAdapter(mutableListOf(), object : BaseAdapter.AdapterListener<Movie> {
            override fun onAdapterItemClicked(position: Int, item: Movie, view: View) {
                MovieDetailFragment.newInstance(item)
                        .show(childFragmentManager, tag)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(topRatedAdapter) {
            moviesFragmentTopRatedRecyclerView.adapter = this
            when (items.isNotEmpty()) {
                true -> moviesFragmentTopRatedTextView.visible()
                false -> moviesFragmentTopRatedTextView.gone()
            }
        }
        moviesFragmentTopRatedTextView.setOnClickListener {
            CategoryFragment.newInstance(CategoryViewModel.Category.TOP_RATED)
                    .show(childFragmentManager, tag)
        }

        with(upcomingAdapter) {
            moviesFragmentUpcomingRecyclerView.adapter = this
            when (items.isNotEmpty()) {
                true -> moviesFragmentUpcomingTextView.visible()
                false -> moviesFragmentUpcomingTextView.gone()
            }
        }

        moviesFragmentUpcomingTextView.setOnClickListener {
            CategoryFragment.newInstance(CategoryViewModel.Category.UPCOMING)
                    .show(childFragmentManager, tag)
        }

        with(popularAdapter) {
            moviesFragmentPopularRecyclerView.adapter = this
            when (items.isNotEmpty()) {
                true -> moviesFragmentPopularTextView.visible()
                false -> moviesFragmentPopularTextView.gone()
            }
        }

        moviesFragmentPopularTextView.setOnClickListener {
            CategoryFragment.newInstance(CategoryViewModel.Category.POPULAR)
                    .show(childFragmentManager, tag)
        }

        moviesFragmentErrorView.initView(object : ErrorView.ErrorViewListener {
            override fun onClick() {
                showContent()
                viewModel.start()
            }
        })

        moviesFragmentSearchBar.setOnQueryTextListener(object : Search.OnQueryTextListener {
            override fun onQueryTextSubmit(query: CharSequence?): Boolean {
                activity?.replaceFragment(SearchFragment.newInstance(query.toString()), R.id.rootLayout, true)
                return true
            }

            override fun onQueryTextChange(newText: CharSequence?) {
                //DO Nothing
            }
        })

        activity?.findViewById<ImageView>(com.lapism.searchview.R.id.search_imageView_logo)?.setImageDrawable(null)
        val searchEditText = activity?.findViewById<EditText>(com.lapism.searchview.R.id.search_searchEditText)
        searchEditText?.hint = "Pesquisar"
        searchEditText?.visible()

    }

    private fun initViewModel() {
        with(viewModel) {
            configurationLive.observe(this@MoviesFragment, Observer {
                when (it.status) {
                    LOADING -> when (it.loading) {
                        true -> showContent()
                        false -> moviesFragmentLoading.gone()
                    }
                    SUCCESS -> {
                        showTitleLabels()
                        loadMovies(it.data!!)
                    }
                    ERROR -> {
                        hideTitleLabels()
                        showErrorView()
                        showError(it.message!!, it.action!!)
                    }
                }
            })

            topRatedLive.observe(this@MoviesFragment, Observer {
                when (it.status) {
                    LOADING -> when (it.loading) {
                        true -> {
                            moviesFragmentTopRatedLoading.show()
                            moviesFragmentTopRatedRecyclerView.gone()
                        }
                        false -> {
                            moviesFragmentTopRatedLoading.hide()
                            moviesFragmentTopRatedRecyclerView.visible()
                        }
                    }
                    SUCCESS -> topRatedAdapter.add(it.data!!.toMutableList(), true)
                    ERROR -> showError(it.message!!, it.action!!)
                }
            })

            upcomingLive.observe(this@MoviesFragment, Observer {
                when (it.status) {
                    LOADING -> when (it.loading) {
                        true -> {
                            moviesFragmentUpcomingLoading.show()
                            moviesFragmentUpcomingRecyclerView.gone()
                        }
                        false -> {
                            moviesFragmentUpcomingLoading.hide()
                            moviesFragmentUpcomingRecyclerView.visible()
                        }
                    }
                    SUCCESS -> upcomingAdapter.add(it.data!!.toMutableList(), true)
                    ERROR -> showError(it.message!!, it.action!!)
                }
            })

            popularLive.observe(this@MoviesFragment, Observer {
                when (it.status) {
                    LOADING -> when (it.loading) {
                        true -> {
                            moviesFragmentPopularLoading.show()
                            moviesFragmentPopularRecyclerView.gone()
                        }
                        false -> {
                            moviesFragmentPopularLoading.hide()
                            moviesFragmentPopularRecyclerView.visible()
                        }
                    }
                    SUCCESS -> popularAdapter.add(it.data!!.toMutableList(), true)
                    ERROR -> showError(it.message!!, it.action!!)
                }
            })

            start()
        }
    }

    private fun showError(message: String, action: () -> Unit) {
        moviesFragmentRootView.showSnackBar(message, Snackbar.LENGTH_LONG, getString(R.string.try_again), action)
    }

    private fun showErrorView() {
        moviesFragmentLoading.gone()
        moviesFragmentErrorView.visible()
    }

    private fun showContent() {
        moviesFragmentLoading.visible()
        moviesFragmentErrorView.gone()
    }

    private fun showTitleLabels() {
        moviesFragmentPopularTextView.visible()
        moviesFragmentTopRatedTextView.visible()
        moviesFragmentUpcomingTextView.visible()
    }

    private fun hideTitleLabels() {
        moviesFragmentPopularTextView.gone()
        moviesFragmentTopRatedTextView.gone()
        moviesFragmentUpcomingTextView.gone()
    }
}
