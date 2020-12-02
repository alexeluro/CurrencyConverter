package com.inspiredcoda.currencyconverter.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.core.view.GravityCompat

fun Context.toast(message: String){
    val centerToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    centerToast.setGravity(Gravity.CENTER, 0, 0)
    centerToast.show()
}