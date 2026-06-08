package com.iiinaiii.punks.presentation.util

import android.widget.ImageView
import coil.load
import coil.size.Precision
import coil.size.ViewSizeResolver

internal fun ImageView.loadImage(url: String?) {
    this.load(url) {
        size(ViewSizeResolver(this@loadImage))
        precision(Precision.EXACT)
    }
}
