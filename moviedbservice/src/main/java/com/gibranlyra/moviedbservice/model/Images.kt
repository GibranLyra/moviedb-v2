package com.gibranlyra.moviedbservice.model

data class Images(var posterSizes: List<String>? = null,
                  var backdropSizes: List<String>? = null,
                  var logoSizes: List<String>? = null,
                  var stillSizes: List<String>? = null,
                  var profileSizes: List<String>? = null,
                  var secureBaseUrl: String? = null,
                  var baseUrl: String? = null)