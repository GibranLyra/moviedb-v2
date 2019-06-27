package com.gibranlyra.moviedb.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gibranlyra.moviedb.R
import com.gibranlyra.moviedb.ui.movie.MoviesFragment
import com.gibranlyra.moviedb.util.ext.addFragment
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun startActivity(context: Context) {
            context.startActivity<MainActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment(MoviesFragment.newInstance(), R.id.rootLayout)
    }
}