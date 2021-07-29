package com.arbo.oracoes.presentation.util.extension

import android.util.TypedValue
import android.widget.TextView

private const val MAX_TEXT_SIZE = 48
private const val MIN_TEXT_SIZE = 14

fun TextView.increaseFontSize() {
    val size = textSize / resources.displayMetrics.scaledDensity
    if (size < MAX_TEXT_SIZE) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size + 1)
    }
}

fun TextView.decreaseFontSize() {
    val size = textSize / resources.displayMetrics.scaledDensity
    if (size > MIN_TEXT_SIZE) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 1)
    }
}

