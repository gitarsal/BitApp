package com.project.bitapp.presentation.pairdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.bitapp.databinding.ItemTradeListBinding
import com.project.bitapp.domain.model.Trade
import com.project.bitapp.utils.PairListDiffCallback
import javax.inject.Inject

class TradeListAdapter @Inject constructor(val clickListener: ClickListener) :
    RecyclerView.Adapter<TradeListAdapter.ViewHolder>() {

    var tradeList: List<Trade> = ArrayList()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tradeList[position]
        holder.bind(item, clickListener)
    }

    override fun getItemCount(): Int {
        return tradeList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTradeListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    fun updateList(tradeList: List<Trade>) {
        val diffCallback = PairListDiffCallback(this.tradeList, tradeList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        this.tradeList = tradeList
    }

    class ViewHolder constructor(private val binding: ItemTradeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Trade, clickListener: ClickListener) {
            binding.data = item
            binding.executePendingBindings()
            binding.clickListener = clickListener
        }
    }
}

class ClickListener @Inject constructor()
