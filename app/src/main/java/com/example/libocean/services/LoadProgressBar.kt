package com.example.libocean.services

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DirectAction
import android.os.Build
import android.view.KeyEvent
import android.widget.LinearLayout
import com.example.libocean.R
import com.example.libocean.`interface`.ProgressBar

class LoadProgressBar(activity: Activity): ProgressBar {
    lateinit var popup: AlertDialog
    var selfActivity = activity
    override fun openProgressBar() {
        var alert = AlertDialog.Builder(selfActivity)
        var view = selfActivity.layoutInflater.inflate(R.layout.load_progress, null)
        alert.setView(view)
        popup = alert.create()
        popup.setCancelable(false)
        popup.window?.setBackgroundDrawableResource(android.R.color.transparent)
        popup.show()
        popup.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (popup.isShowing) {
                    dialog.dismiss()
                }
            }
            true
        }
    }

    override fun closeProgressBar() {
        if (popup.isShowing) {
            popup.dismiss()
        }
    }
}