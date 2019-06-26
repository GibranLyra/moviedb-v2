package com.gibranlyra.moviedbservice.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Configuration(@PrimaryKey(autoGenerate = true)
                         val uId: Int,
                         @Embedded(prefix = "configuration_images")
                         val images: Images? = null,
                         @Embedded(prefix = "configuration_change_keys")
                         val changeKeys: ArrayList<String>? = null)