package com.gibranlyra.moviedbservice.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Language(val name: String? = null, val iso6391: String? = null): Parcelable