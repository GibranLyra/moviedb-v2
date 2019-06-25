package com.gibranlyra.moviedb.ui.main

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
import com.gibranlyra.moviedb.util.ext.showSnackBar
import com.gibranlyra.moviedb.util.resource.ResourceState.*
import com.gibranlyra.moviedbservice.model.Movie
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders
                .of(this, ViewModelFactory.getInstance(MyApp.instance))
                .get(MainViewModel::class.java)
    }

    private val popularMovieAdapter by lazy {
        MovieAdapter(mutableListOf(), object : BaseAdapter.AdapterListener<Movie> {
            override fun onAdapterItemClicked(position: Int, item: Movie, view: View) {

            }
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
        mainFragmentPopularRecyclerView.adapter = popularMovieAdapter
    }

    private fun initViewModel() {
        with(viewModel) {
            movieLive.observe(this@MainFragment, Observer {
                when (it.status) {
                    LOADING -> {
                        when (it.loading) {
                            true -> mainFragmentLoading.show()
                            false -> mainFragmentLoading.hide()
                        }

                    }
                    SUCCESS -> popularMovieAdapter.add(it.data!!.toMutableList(), true)
                    ERROR -> mainFragmentRootView.showSnackBar(it.message!!, Snackbar.LENGTH_LONG,
                            "Tentar novamente", it.callback!!)
                }
            })

            loadMovies()
        }
    }
}