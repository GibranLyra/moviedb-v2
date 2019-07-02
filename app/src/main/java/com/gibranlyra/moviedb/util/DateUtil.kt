package com.gibranlyra.moviedb.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    @JvmStatic
    fun convertToFriendlyDate(serverDate: String): String {
        var format = SimpleDateFormat("yyyy-MM-dd")
        var newDate: Date? = null
        try {
            newDate = format.parse(serverDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(newDate)
    }
}

