package com.iiinaiii.punks.presentation.detail.item

import android.view.View
import com.iiinaiii.punks.presentation.R
import com.iiinaiii.punks.presentation.databinding.ItemDetailHeaderBinding
import com.xwray.groupie.viewbinding.BindableItem

internal class HeaderItem(private val text: String) : BindableItem<ItemDetailHeaderBinding>() {
    override fun getLayout() = R.layout.item_detail_header

    override fun bind(viewBinding: ItemDetailHeaderBinding, position: Int) {
        viewBinding.headerText.text = text
    }

    override fun initializeViewBinding(view: View) = ItemDetailHeaderBinding.bind(view)
}
