package com.gibranlyra.moviedb.util.ext

import com.gibranlyra.moviedbservice.model.Images
import com.gibranlyra.moviedbservice.model.Movie

fun Movie.buildImages(images: Images): Movie {
    return if (posterPath!!.startsWith("http")) {
        this
    } else {
        copy(posterPath = "${images.builPosterUrl()}$posterPath", backdropPath = "${images.builProfileUrl()}$backdropPath")
    }
}
