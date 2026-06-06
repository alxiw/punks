package com.iiinaiii.punks.presentation.detail.item

import android.view.View
import com.iiinaiii.punks.presentation.R
import com.iiinaiii.punks.presentation.databinding.ItemDetailValueBinding
import com.xwray.groupie.viewbinding.BindableItem

class ValueItem(
    private val title: String,
    private val value: String
) : BindableItem<ItemDetailValueBinding>() {
    override fun getLayout() = R.layout.item_detail_value

    override fun bind(viewBinding: ItemDetailValueBinding, position: Int) {
        viewBinding.title.text = title
        viewBinding.value.text = value
    }

    override fun initializeViewBinding(view: View) = ItemDetailValueBinding.bind(view)
}
