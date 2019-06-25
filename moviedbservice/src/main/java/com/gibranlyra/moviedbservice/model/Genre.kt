package com.gibranlyra.moviedbservice.model

import androidx.room.Entity

@Entity
data class Genre(var name: String? = null, var id: Int = 0)