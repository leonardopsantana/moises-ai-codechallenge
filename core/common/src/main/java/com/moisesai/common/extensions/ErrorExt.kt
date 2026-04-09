package com.moisesai.common.extensions

import android.content.Context
import android.widget.Toast

fun showErrorMessage(context: Context, errorMessage: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(context, errorMessage, duration).show()
}