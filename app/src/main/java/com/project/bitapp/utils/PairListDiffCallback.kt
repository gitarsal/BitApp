package com.project.bitapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.project.bitapp.domain.model.Trade

open class PairListDiffCallback(
    private val oldTradeList: List<Trade>,
    private val newTradelist: List<Trade>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldTradeList.size
    }

    override fun getNewListSize(): Int {
        return newTradelist.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTradeList[oldItemPosition] == newTradelist[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTradeList[oldItemPosition] == newTradelist[newItemPosition]
    }
}
