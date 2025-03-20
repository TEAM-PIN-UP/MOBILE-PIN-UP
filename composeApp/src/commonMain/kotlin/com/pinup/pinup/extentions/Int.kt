package com.pinup.pinup.extentions

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat

fun Float.toDp(context: Context): Float {
    val metric = context.resources.displayMetrics
    return (this / (metric.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
}

fun Int.toDp(context: Context): Float {
    val metric = context.resources.displayMetrics
    return (this / (metric.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
}

fun Int.toPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

fun Int.twoDigit(): String {
    return this.toString().padStart(2, '0')
}
