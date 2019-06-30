package com.gibranlyra.moviedb.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gibranlyra.moviedb.MyApp
import com.gibranlyra.moviedb.R
import com.gibranlyra.moviedb.di.ViewModelFactory
import com.gibranlyra.moviedb.ui.component.movie.BaseAdapter
import com.gibranlyra.moviedb.ui.component.movie.MovieAdapter
import com.gibranlyra.moviedb.util.ext.gone
import com.gibranlyra.moviedb.util.ext.showSnackBar
import com.gibranlyra.moviedb.util.ext.visible
import com.gibranlyra.moviedb.util.resource.ResourceState.*
import com.gibranlyra.moviedbservice.model.Movie
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*

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
        MovieAdapter(mutableListOf(), object : BaseAdapter.AdapterListener<Movie> {
            override fun onAdapterItemClicked(position: Int, item: Movie, view: View) {}
        })
    }

    private val upcomingAdapter by lazy {
        MovieAdapter(mutableListOf(), object : BaseAdapter.AdapterListener<Movie> {
            override fun onAdapterItemClicked(position: Int, item: Movie, view: View) {}
        })
    }

    private val popularAdapter by lazy {
        MovieAdapter(mutableListOf(), object : BaseAdapter.AdapterListener<Movie> {
            override fun onAdapterItemClicked(position: Int, item: Movie, view: View) {}
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainFragmentTopRatedRecyclerView.adapter = topRatedAdapter
        mainFragmentUpcomingRecyclerView.adapter = upcomingAdapter
        mainFragmentPopularRecyclerView.adapter = popularAdapter
    }

    private fun initViewModel() {
        with(viewModel) {
            configurationLive.observe(this@MoviesFragment, Observer {
                when (it.status) {
                    LOADING -> when (it.loading) {
                        true -> mainFragmentLoading.visible()
                        false -> mainFragmentLoading.gone()
                    }
                    SUCCESS -> loadMovies(it.data!!)
                    ERROR -> showError(it.message!!, it.action!!)
                }
            })

            topRatedLive.observe(this@MoviesFragment, Observer {
                when (it.status) {
                    LOADING -> when (it.loading) {
                        true -> {
                            mainFragmentTopRatedLoading.show()
                            mainFragmentTopRatedRecyclerView.gone()
                        }
                        false -> {
                            mainFragmentTopRatedLoading.hide()
                            mainFragmentTopRatedRecyclerView.visible()
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
                            mainFragmentUpcomingLoading.show()
                            mainFragmentUpcomingRecyclerView.gone()
                        }
                        false -> {
                            mainFragmentUpcomingLoading.hide()
                            mainFragmentUpcomingRecyclerView.visible()
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
                            mainFragmentPopularLoading.show()
                            mainFragmentPopularRecyclerView.gone()
                        }
                        false -> {
                            mainFragmentPopularLoading.hide()
                            mainFragmentPopularRecyclerView.visible()
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
        mainFragmentRootView.showSnackBar(message, Snackbar.LENGTH_LONG, getString(R.string.try_again), action)
    }
}