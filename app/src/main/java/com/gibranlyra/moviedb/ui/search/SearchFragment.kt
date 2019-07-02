package com.gibranlyra.moviedb.ui.search

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
import com.gibranlyra.moviedb.ui.component.BaseAdapter
import com.gibranlyra.moviedb.ui.component.error.ErrorView
import com.gibranlyra.moviedb.ui.component.movie.MovieAdapter
import com.gibranlyra.moviedb.ui.moviedetail.MovieDetailFragment
import com.gibranlyra.moviedb.util.ext.gone
import com.gibranlyra.moviedb.util.ext.replaceFragment
import com.gibranlyra.moviedb.util.ext.showSnackBar
import com.gibranlyra.moviedb.util.ext.visible
import com.gibranlyra.moviedb.util.resource.ResourceState.*
import com.gibranlyra.moviedbservice.model.Movie
import com.google.android.material.snackbar.Snackbar
import com.lapism.searchview.Search
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    private val viewModel: SearchViewModel by lazy {
        ViewModelProviders
                .of(this, ViewModelFactory.getInstance(MyApp.instance))
                .get(SearchViewModel::class.java)
    }

    private val searchAdapter by lazy {
        MovieAdapter(mutableListOf(), object : BaseAdapter.AdapterListener<Movie> {
            override fun onAdapterItemClicked(position: Int, item: Movie, view: View) {
                activity?.replaceFragment(MovieDetailFragment.newInstance(item), R.id.rootLayout, true)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchFragmentRecyclerView.adapter = searchAdapter

        searchFragmentErrorView.initView(object : ErrorView.ErrorViewListener {
            override fun onClick() {
                showContent()
                viewModel.start()
            }
        })

        searchFragmentSearchBar.setOnQueryTextListener(object : Search.OnQueryTextListener {
            override fun onQueryTextSubmit(query: CharSequence?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: CharSequence?) {
                //DO Nothing
            }

        })
    }

    private fun initViewModel() {
        with(viewModel) {
            configurationLive.observe(this@SearchFragment, Observer {
                when (it.status) {
                    LOADING -> when (it.loading) {
                        true -> showContent()
                        false -> {
                            //Do Nothing
                        }
                    }
                    SUCCESS -> {
                        loadMovies(it.data!!, "Homem Aranha")
                    }
                    ERROR -> {
                        showErrorView()
                        showError(it.message!!, it.action!!)
                    }
                }
            })


            searchLive.observe(this@SearchFragment, Observer {
                when (it.status) {
                    LOADING -> when (it.loading) {
                        true -> {
                            searchFragmentLoading.show()
                            searchFragmentRecyclerView.gone()
                        }
                        false -> {
                            searchFragmentLoading.hide()
                            searchFragmentRecyclerView.visible()
                        }
                    }
                    SUCCESS -> {
                        searchAdapter.add(it.data!!.toMutableList(), true)
                    }
                    ERROR -> showError(it.message!!, it.action!!)
                }
            })

            start()
        }
    }

    private fun showError(message: String, action: () -> Unit) {
        searchFragmentRootView.showSnackBar(message, Snackbar.LENGTH_LONG, getString(R.string.try_again), action)
    }

    private fun showErrorView() {
        searchFragmentLoading.gone()
        searchFragmentErrorView.visible()
    }

    private fun showContent() {
        searchFragmentLoading.visible()
        searchFragmentErrorView.gone()
    }
}
