package com.gibranlyra.moviedb.ui.component.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gibranlyra.moviedb.R
import com.gibranlyra.moviedb.ui.component.BaseAdapter
import com.gibranlyra.moviedb.ui.component.movie.HorizontalMovieAdapter.ViewHolder
import com.gibranlyra.moviedb.util.ext.loadImage
import com.gibranlyra.moviedbservice.model.Movie
import kotlinx.android.synthetic.main.item_movie_horizontal.view.*

class HorizontalMovieAdapter(items: MutableList<Movie>, listener: AdapterListener<Movie>) : BaseAdapter<Movie, ViewHolder,
        BaseAdapter.AdapterListener<Movie>>(items, listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie_horizontal, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        with(holder.itemView) {
            movieItemHorizontalRootView.setOnClickListener { listener?.onAdapterItemClicked(position, items[position], this) }
        }
    }

    inner class ViewHolder(itemView: View) : BaseAdapter.ViewHolder<Movie>(itemView) {
        override fun bind(item: Movie) {
            with(itemView) {
                movieItemHorizontalImageView.loadImage(item.posterPath ?: "")
                movieItemHorizontalTitleView.text = item.originalTitle!!
            }
        }
    }
}