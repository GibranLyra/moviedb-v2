package com.gibranlyra.moviedb.util.ext

import com.gibranlyra.moviedbservice.model.Images

fun Images.builPosterUrl() = "$baseUrl${posterSizes?.get(3)}"

fun Images.builProfileUrl() = "$baseUrl${logoSizes?.get(1)}"