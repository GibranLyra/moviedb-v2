package com.gibranlyra.moviedbservice.model

import androidx.room.Entity

@Entity
data class Genre(val name: String? = null, val id: Int = 0)