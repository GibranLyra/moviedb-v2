package com.gibranlyra.moviedb.util.ext

import com.gibranlyra.moviedbservice.model.Images
import com.gibranlyra.moviedbservice.model.Movie

fun Movie.buildImages(images: Images) = copy(posterPath = "${images.builPosterUrl()}$posterPath",
        backdropPath = "${images.builProfileUrl()}$backdropPath")
