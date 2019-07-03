package com.gibranlyra.moviedb.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.gibranlyra.moviedb.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialog: BottomSheetDialogFragment() {

    var backButton: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.SheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog

            val bottomSheet = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            val sheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
            sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> dismiss()
                        BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_DRAGGING, BottomSheetBehavior.STATE_HALF_EXPANDED, BottomSheetBehavior.STATE_SETTLING -> {
                            //DO Nothing
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (isAdded) {
                        backButton?.rotation = slideOffset * 90
                    }
                }
            })
        }
        return dialog
    }

}