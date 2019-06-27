package com.gibranlyra.moviedbservice.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(val name: String? = null, val id: Int = 0): Parcelable