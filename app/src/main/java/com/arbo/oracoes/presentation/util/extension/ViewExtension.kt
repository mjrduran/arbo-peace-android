package com.arbo.oracoes.presentation.util.extension


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.becomeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.disable() {
    this.isEnabled = false
}

fun View.enable() {
    this.isEnabled = true
}

fun View.displaySnackbar(@StringRes message: Int) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.displaySnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.toBitmap(): Bitmap {
    val returnedBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(returnedBitmap)
    val bgDrawable: Drawable? = this.background
    if (bgDrawable != null) {
        bgDrawable.draw(canvas)
    } else {
        canvas.drawColor(Color.WHITE)
    }
    this.draw(canvas)
    return returnedBitmap
}
