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
import com.gibranlyra.moviedb.util.ext.showSnackBar
import com.gibranlyra.moviedb.util.resource.ResourceState.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_main, container, false)

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
                    SUCCESS -> {
                        Timber.d("initViewModel: ")
                    }
                    ERROR -> mainFragmentRootView.showSnackBar(it.message!!, Snackbar.LENGTH_LONG,
                            "Tentar novamente", it.callback!!)
                }
            })

            loadMovies()
        }
    }
}