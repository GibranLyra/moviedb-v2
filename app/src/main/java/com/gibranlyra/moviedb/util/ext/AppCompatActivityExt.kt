package com.gibranlyra.moviedb.util.ext


import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import timber.log.Timber


fun FragmentActivity.replaceFragment(fragment: Fragment, frameId: Int, addToBackStack: Boolean = false) {
    supportFragmentManager.beginTransaction().apply {
        setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        setTransition(TRANSIT_FRAGMENT_FADE)
        replace(frameId, fragment)
        setPrimaryNavigationFragment(fragment)
        if (addToBackStack) {
            addToBackStack(fragment.javaClass.simpleName)
        }
    }.commitAllowingStateLoss()
}

fun AppCompatActivity.addFragment(fragment: Fragment, @IdRes containerViewId: Int) {
    supportFragmentManager.beginTransaction().apply {
        add(containerViewId, fragment)
    }.commitAllowingStateLoss()
}

fun FragmentActivity.requiredBundleNotFound(bundleNotFoundName: String) {
    Timber.e("Bundle %s is required and was not found", bundleNotFoundName)
    finish()
}
