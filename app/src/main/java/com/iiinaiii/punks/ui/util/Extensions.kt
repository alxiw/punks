package com.iiinaiii.punks.ui.util

import android.view.View
import android.widget.ImageView
import coil.load
import coil.size.Precision
import coil.size.ViewSizeResolver

fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun ImageView.loadImage(url: String?) {
    this.load(url) {
        size(ViewSizeResolver(this@loadImage))
        precision(Precision.EXACT)
    }
}
