package com.gibranlyra.moviedbservice.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(val iso31661: String? = null, val name: String? = null): Parcelable