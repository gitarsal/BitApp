package com.project.bitapp.presentation.pairlisting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.bitapp.databinding.ItemPairListBinding
import com.project.bitapp.domain.model.AllPairItem
import javax.inject.Inject
import kotlin.collections.ArrayList

class PairListingAdapter @Inject constructor(val clickListener: ClickListener) :
    RecyclerView.Adapter<PairListingAdapter.ViewHolder>() {

    private var pairList: List<AllPairItem> = ArrayList()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = pairList[position]
        holder.bind(item, clickListener)
    }

    override fun getItemCount(): Int {
        return pairList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    fun addData(pairList: List<AllPairItem>) {
        this.pairList = pairList
        notifyDataSetChanged()
    }

    class ViewHolder private constructor(private val binding: ItemPairListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AllPairItem, clickListener: ClickListener) {
            binding.data = item
            binding.executePendingBindings()
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPairListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ClickListener @Inject constructor() {

    var onItemClick: ((AllPairItem) -> Unit)? = null

    fun onClick(data: AllPairItem) {
        onItemClick?.invoke(data)
    }
}
