package com.gibranlyra.moviedb.util.ext

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.addFragment(fragment: androidx.fragment.app.Fragment, @IdRes containerViewId: Int) {
    supportFragmentManager.beginTransaction().apply {
        add(containerViewId, fragment)
    }.commitAllowingStateLoss()
}