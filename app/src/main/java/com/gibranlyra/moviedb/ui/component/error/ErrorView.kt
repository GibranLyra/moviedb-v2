package com.gibranlyra.moviedb.ui.component.error

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.gibranlyra.moviedb.R
import kotlinx.android.synthetic.main.view_error.view.*

class ErrorView @JvmOverloads constructor(context: Context,
                                          attrs: AttributeSet? = null,
                                          defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_error, this, true)
    }

    fun initView(listener: ErrorViewListener) {
        errorViewTryAgainButton.setOnClickListener { listener.onClick() }
    }

    interface ErrorViewListener {
        fun onClick()
    }
}