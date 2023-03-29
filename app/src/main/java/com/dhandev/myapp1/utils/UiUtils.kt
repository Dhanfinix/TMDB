package com.dhandev.myapp1.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import com.dhandev.myapp1.R
import com.google.android.material.snackbar.Snackbar

class UiUtils {
    fun showLoading(context: Context) : Dialog{
        val loading = Dialog(context)
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loading.setContentView(R.layout.loading_popup)
        loading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loading.show()
        return loading
    }

    fun showSnackBar(context: Context, parentView: View, message: String, actionText: String, duration: Int, myCallback: () -> Unit) {
        return Snackbar.make(parentView, message, duration)
            .setAction(actionText) {
                myCallback.invoke()
            }
            .setBackgroundTint(Color.WHITE)
            .setTextColor(Color.BLACK)
            .setActionTextColor(ContextCompat.getColor(context, R.color.green_500))
            .show()
    }

    fun showAlert(context: Context, title: String, message: String, positiveText: String, negativeText:String, positiveCallback: () -> Unit, negativeCallback: (AlertDialog) -> Unit) {
        // Create an alert builder
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)
        var dialog: AlertDialog? = null

        // add a button
        builder.setPositiveButton(positiveText) { _, _ ->
            positiveCallback.invoke()
        }
        builder.setNegativeButton(negativeText) { _, _ ->
            dialog?.let { negativeCallback.invoke(it) }
        }
        dialog = builder.create()
        dialog?.show()
    }
}