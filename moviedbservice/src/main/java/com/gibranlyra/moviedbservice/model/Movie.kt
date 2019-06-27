package com.gibranlyra.moviedbservice.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Movie(
        @PrimaryKey(autoGenerate = true)
        val uId: Int,
        val originalLanguage: String? = null,
        val imdbId: String? = null,
        val isVideo: Boolean = false,
        val title: String? = null,
        val backdropPath: String? = null,
        val revenue: Int? = null,
        @Embedded(prefix = "movie_genres")
        val genres: ArrayList<Genre>? = null,
        val popularity: Double? = null,
        @Embedded(prefix = "movie_countries")
        val movieCountries: ArrayList<Country>? = null,
        @ColumnInfo(name = "id", index = true)
        val id: Int,
        val voteCount: Int = 0,
        val budget: Int = 0,
        val overview: String? = null,
        val originalTitle: String? = null,
        val runtime: Int? = null,
        val posterPath: String? = null,
        @Embedded(prefix = "movie_languages")
        val spokenLanguages: ArrayList<Language>? = null,
        @Embedded(prefix = "movie_companies")
        val movieCompanies: ArrayList<Company>? = null,
        val releaseDate: String? = null,
        val voteAverage: Double? = null,
        val tagline: String? = null,
        val isAdult: Boolean = false,
        val homepage: String? = null,
        val status: String? = null):Parcelable