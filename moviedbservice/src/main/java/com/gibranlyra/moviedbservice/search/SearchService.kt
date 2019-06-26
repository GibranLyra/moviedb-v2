package com.gibranlyra.moviedbservice.search

import com.gibranlyra.moviedbservice.model.Movie
import com.gibranlyra.moviedbservice.model.MovieDbResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

internal interface SearchService {
    @GET("search/movie")
    fun getMovies(@Query("page") page: Int,
                  @Query("query") query: String): Single<MovieDbResponse<Movie>>
}