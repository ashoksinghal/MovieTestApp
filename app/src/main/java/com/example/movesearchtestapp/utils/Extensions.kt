package com.example.movesearchtestapp.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

inline fun String?.toToast(context: Context, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, duration).show()
}

fun View.showSnackBarOnError(snackbarText: String, timeLength: Int=Snackbar.LENGTH_SHORT) {
     Snackbar.make(this, snackbarText, timeLength).show()
//    val snackBarView = snackBar.view
//    snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.snack_bar_bg_when_error))
//    val textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
//    textView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
//    snackBar.show()
}