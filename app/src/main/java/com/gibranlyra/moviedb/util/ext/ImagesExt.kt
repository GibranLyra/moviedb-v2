package com.gibranlyra.moviedb.util.ext

import com.gibranlyra.moviedbservice.model.Images

fun Images.buildPosterUrl() = "$baseUrl${posterSizes?.get(3)}"

fun Images.buildProfileUrl() = "$baseUrl${logoSizes?.get(3)}"