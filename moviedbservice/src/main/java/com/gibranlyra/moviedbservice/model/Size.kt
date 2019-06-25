package com.gibranlyra.moviedbservice.model

import com.google.gson.annotations.SerializedName

data class Size(@SerializedName("available")
                var isAvailable: Boolean = false,
                var size: String? = null,
                var sku: String? = null)