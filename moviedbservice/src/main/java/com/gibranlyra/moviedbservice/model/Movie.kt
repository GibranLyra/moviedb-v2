package com.gibranlyra.moviedbservice.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
        @PrimaryKey(autoGenerate = true)
        var uId: Int,
        var originalLanguage: String? = null,
        var imdbId: String? = null,
        var isVideo: Boolean = false,
        var title: String? = null,
        var backdropPath: String? = null,
        var revenue: Int? = null,
        @Embedded(prefix = "movie_genres")
        var genres: ArrayList<Genre>? = null,
        var popularity: Double? = null,
        @Embedded(prefix = "movie_countries")
        var movieCountries: ArrayList<Country>? = null,
        @ColumnInfo(name = "id", index = true)
        var id: Int,
        var voteCount: Int = 0,
        var budget: Int = 0,
        var overview: String? = null,
        var originalTitle: String? = null,
        var runtime: Int? = null,
        var posterPath: String? = null,
        @Embedded(prefix = "movie_languages")
        var spokenLanguages: ArrayList<Language>? = null,
        @Embedded(prefix = "movie_companies")
        var movieiCompanies: ArrayList<Company>? = null,
        var releaseDate: String? = null,
        var voteAverage: Double? = null,
        var tagline: String? = null,
        var isAdult: Boolean = false,
        var homepage: String? = null,
        var status: String? = null)