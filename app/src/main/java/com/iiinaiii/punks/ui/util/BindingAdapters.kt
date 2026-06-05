package com.iiinaiii.punks.ui.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Precision
import coil.size.ViewSizeResolver

@BindingAdapter("visibleUnless")
fun bindVisibleUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    this.load(url) {
        size(ViewSizeResolver(this@loadImage))
        precision(Precision.EXACT)
    }
}
