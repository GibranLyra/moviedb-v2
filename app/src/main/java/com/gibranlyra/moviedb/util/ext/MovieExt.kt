package com.gibranlyra.moviedb.util.ext

import com.gibranlyra.moviedbservice.model.Images
import com.gibranlyra.moviedbservice.model.Movie
import timber.log.Timber

fun Movie.buildImages(images: Images): Movie {
    posterPath?.let {
        return if (posterPath!!.startsWith("http")) {
            this
        } else {
            copy(posterPath = "${images.buildPosterUrl()}$posterPath", backdropPath = "${images.buildProfileUrl()}$backdropPath")
        }
    } ?: run {
        Timber.w("buildImages: PosterPath for movie ${this.title} is null.")
        return this@buildImages
    }
}
