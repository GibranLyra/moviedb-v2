package com.gibranlyra.moviedb.ui.component.error

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_error.view.*

class ErrorView @JvmOverloads constructor(context: Context,
                                          attrs: AttributeSet? = null,
                                          defStyleAttr: Int = 0,
                                          private var listener: ErrorViewListener? = null)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        errorViewTryAgainButton.setOnClickListener { listener?.onClick() }
    }

    fun initView(listener: ErrorViewListener) {
        this.listener = listener
    }

    interface ErrorViewListener {
        fun onClick()
    }
}