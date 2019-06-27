package com.gibranlyra.moviedb.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gibranlyra.moviedb.R
import com.gibranlyra.moviedb.util.ext.requiredBundleNotFound
import com.gibranlyra.moviedbservice.model.Movie

const val EXTRA_MOVIE = "EXTRA_MOVIE"

class MovieDetailFragment : Fragment() {
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

    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<Movie>(EXTRA_MOVIE)?.let {
            movie = it
        } ?: run {
            activity?.requiredBundleNotFound(EXTRA_MOVIE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_movie_detail, container, false)
}