package com.gibranlyra.moviedb.util.ext

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import timber.log.Timber

fun FragmentActivity.addFragment(fragment: androidx.fragment.app.Fragment, @IdRes containerViewId: Int) {
    supportFragmentManager.beginTransaction().apply {
        add(containerViewId, fragment)
    }.commitAllowingStateLoss()
}

fun FragmentActivity.requiredBundleNotFound(bundleNotFoundName: String) {
    Timber.e("Bundle %s is required and was not found", bundleNotFoundName)
    finish()
}
