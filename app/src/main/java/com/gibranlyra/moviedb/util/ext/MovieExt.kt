package com.gibranlyra.moviedb.util.ext

import com.gibranlyra.moviedbservice.model.Images
import com.gibranlyra.moviedbservice.model.Movie
import timber.log.Timber

fun Movie.buildImages(images: Images): Movie {
    posterPath?.let {
        return if (posterPath!!.startsWith("http")) {
            this
        } else {
            copy(posterPath = "${images.builPosterUrl()}$posterPath", backdropPath = "${images.builProfileUrl()}$backdropPath")
        }
    } ?:run {
        Timber.d("buildImages: $this")
        return this@buildImages
    }
}
