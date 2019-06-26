package com.gibranlyra.moviedbservice.model

import androidx.room.Embedded

data class Images(@Embedded(prefix = "images_poster_sizes")
                  val posterSizes: ArrayList<String>? = null,
                  @Embedded(prefix = "images_backdrop_sizes")
                  val backdropSizes: ArrayList<String>? = null,
                  @Embedded(prefix = "images_logo_sizes")
                  val logoSizes: ArrayList<String>? = null,
                  @Embedded(prefix = "images_still_sizes")
                  val stillSizes: ArrayList<String>? = null,
                  @Embedded(prefix = "images_profile_sizes")
                  val profileSizes: ArrayList<String>? = null,
                  val secureBaseUrl: String? = null,
                  val baseUrl: String? = null)