package com.gibranlyra.moviedb.ui.component.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gibranlyra.moviedb.R
import com.gibranlyra.moviedb.ui.component.movie.MovieAdapter.ViewHolder
import com.gibranlyra.moviedbservice.model.Movie

class MovieAdapter(val items: MutableList<Movie>) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Movie) {

        }
    }
}