package com.gibranlyra.moviedb.util.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.gibranlyra.moviedb.R

fun ImageView.loadImage(url: String) {
    Glide.with(this)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(this)
}