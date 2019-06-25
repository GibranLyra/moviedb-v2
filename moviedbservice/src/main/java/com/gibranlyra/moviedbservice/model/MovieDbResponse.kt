package com.gibranlyra.moviedbservice.model

data class MovieDbResponse(var page: Int? = null,
                           var totalPages: Int? = null,
                           var results: List<Movie>,
                           var totalResults: Int? = null)