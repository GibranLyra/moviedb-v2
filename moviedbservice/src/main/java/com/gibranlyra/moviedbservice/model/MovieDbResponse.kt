package com.gibranlyra.moviedbservice.model

data class MovieDbResponse<T>(var page: Int? = null,
                           var totalPages: Int? = null,
                           var results: List<T>,
                           var totalResults: Int? = null)