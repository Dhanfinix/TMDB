package com.dhandev.myapp1.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.dhandev.myapp1.R

class uiUtil {
    fun showLoading(context: Context) : Dialog{
        val loading = Dialog(context)
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loading.setContentView(R.layout.loading_popup)
        loading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loading.show()
        return loading
    }
}