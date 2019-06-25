package com.gibranlyra.moviedb.util.ext

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(message: String, duration: Int, actionText: String, actionAction: () -> Unit) {
    Snackbar.make(this, message, duration)
            .setAction(actionText) { actionAction() }
            .show()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}