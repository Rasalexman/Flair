package com.mincor.flair.utils

import android.view.View
import org.jetbrains.anko.dip

var margin16: Int = 0
var margin8 = 0
var margin4 = 0
fun View.dip16(): Int {
    if (margin16 == 0) {
        margin16 = dip(16)
    }
    return margin16
}

fun View.dip8(): Int {
    if (margin8 == 0) {
        margin8 = dip(8)
    }
    return margin8
}

fun View.dip4(): Int {
    if (margin4 == 0) {
        margin4 = dip(4)
    }
    return margin4
}