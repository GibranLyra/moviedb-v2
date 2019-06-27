package com.gibranlyra.moviedbservice.model

data class Images(val posterSizes: ArrayList<String>? = null,
                  val backdropSizes: ArrayList<String>? = null,
                  val logoSizes: ArrayList<String>? = null,
                  val stillSizes: ArrayList<String>? = null,
                  val profileSizes: ArrayList<String>? = null,
                  val secureBaseUrl: String? = null,
                  val baseUrl: String? = null)