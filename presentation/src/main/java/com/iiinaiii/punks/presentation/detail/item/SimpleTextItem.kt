package com.iiinaiii.punks.presentation.detail.item

import android.view.View
import com.iiinaiii.punks.presentation.R
import com.iiinaiii.punks.presentation.databinding.ItemDetailSimpleTextBinding
import com.xwray.groupie.viewbinding.BindableItem

class SimpleTextItem(private val text: String) : BindableItem<ItemDetailSimpleTextBinding>() {
    override fun getLayout() = R.layout.item_detail_simple_text

    override fun bind(viewBinding: ItemDetailSimpleTextBinding, position: Int) {
        viewBinding.bodyText.text = text
    }

    override fun initializeViewBinding(view: View) = ItemDetailSimpleTextBinding.bind(view)
}
