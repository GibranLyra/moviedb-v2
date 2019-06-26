package com.gibranlyra.moviedbservice.model

import androidx.room.Embedded

data class Images(@Embedded(prefix = "images_poster_sizes")
                  var posterSizes: ArrayList<String>? = null,
                  @Embedded(prefix = "images_backdrop_sizes")
                  var backdropSizes: ArrayList<String>? = null,
                  @Embedded(prefix = "images_logo_sizes")
                  var logoSizes: ArrayList<String>? = null,
                  @Embedded(prefix = "images_still_sizes")
                  var stillSizes: ArrayList<String>? = null,
                  @Embedded(prefix = "images_profile_sizes")
                  var profileSizes: ArrayList<String>? = null,
                  var secureBaseUrl: String? = null,
                  var baseUrl: String? = null)