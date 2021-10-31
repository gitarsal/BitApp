package com.project.bitapp.presentation.pairdetail

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.bitapp.MainActivity
import com.project.bitapp.R
import com.project.bitapp.databinding.DialogPairListViewBinding
import com.project.bitapp.databinding.FragmentPairDetailBinding
import com.project.bitapp.domain.model.Trade
import com.project.bitapp.presentation.pairlisting.PairListingAdapter
import com.project.bitapp.presentation.viewmodel.PairListingAndDetailViewModel
import com.project.bitapp.utils.REFRESH_KEY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PairDetailFragment : Fragment() {
    private val pairListingAndDetailViewModel: PairListingAndDetailViewModel by activityViewModels()
    private lateinit var binding: FragmentPairDetailBinding

    @Inject
    lateinit var adapter: PairListingAdapter

    @Inject
    lateinit var tradeListAdapter: TradeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPairDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitleValues()
        setSubscribeTicketData()
        setupTradesSection()
        setClickListeners()
        (requireActivity() as MainActivity).setSupportActionBar(binding.toolBar)
        binding.toolBar.setupWithNavController(findNavController())
        binding.toolBar.setNavigationIcon(R.drawable.back_arrow)
    }

    private fun setupTradesSection() {
        setTradesAdapter()
        pairListingAndDetailViewModel.onCurrentSubscribeTradeData.observe(
            viewLifecycleOwner,
            { newTrade ->

                if (newTrade.id == REFRESH_KEY) {
                    tradeListAdapter.updateList(pairListingAndDetailViewModel.tradesList)
                    binding.tradesGroup.visibility = View.GONE
                    return@observe
                }

                checkVisibilityOfTradesRecyclerView()

                pairListingAndDetailViewModel.tradesList.add(newTrade)
                tradeListAdapter.updateList(pairListingAndDetailViewModel.tradesList)
                binding.tradesRv.scrollToPosition(pairListingAndDetailViewModel.tradesList.size - 1)
            }
        )
    }

    private fun checkVisibilityOfTradesRecyclerView() {

        if (pairListingAndDetailViewModel.tradesList.size == 0) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tradesGroup.visibility = View.GONE
        } else {
            binding.tradesGroup.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setClickListeners() {
        binding.switchBtn.setOnClickListener {
            showPairList(requireActivity())
        }
    }

    private fun renderList(updatedList: List<Trade>) {
        tradeListAdapter.apply {
            updateList(updatedList)
        }
    }

    private fun setTradesAdapter() {
        binding.tradesRv.apply {
            this.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = tradeListAdapter
        }

        renderList(pairListingAndDetailViewModel.tradesList)
    }

    private fun setSubscribeTicketData() {

        pairListingAndDetailViewModel.onCurrentSubscribeTickerData.observe(
            viewLifecycleOwner,
            { pair ->
                binding.price.text = pair.openPrice
                binding.row1.columnOne.value.text = pair.openPrice
                binding.row1.columnTwo.value.text = pair.dailyChange
                binding.row2.columnOne.value.text = pair.bid
                binding.row2.columnTwo.value.text = pair.ask
                binding.row3.columnOne.value.text = pair.openPrice
                binding.row3.columnTwo.value.text =
                    pair.high.plus("~").plus(pair.low)
            }
        )
    }

    private fun setTitleValues() {
        pairListingAndDetailViewModel.onCurrentSubscribePairItem.observe(
            viewLifecycleOwner,
            { pair ->
                binding.toolBar.title = pair.name
            }
        )
        binding.row1.columnOne.title.text =
            requireContext().resources.getString(R.string.open_price_title)
        binding.row1.columnTwo.title.text =
            requireContext().resources.getString(R.string.daily_change_title)
        binding.row2.columnOne.title.text =
            requireContext().resources.getString(R.string.top_bid_title)
        binding.row2.columnTwo.title.text =
            requireContext().resources.getString(R.string.lowest_ask_title)
        binding.row3.columnOne.title.text =
            requireContext().resources.getString(R.string.last_price_title)
        binding.row3.columnTwo.title.text =
            requireContext().resources.getString(R.string.range_title)
        binding.tradeItemsHeading.amountTitle.text =
            requireContext().resources.getString(R.string.amount)
        binding.tradeItemsHeading.priceTitle.text =
            requireContext().resources.getString(R.string.price)
        binding.tradeItemsHeading.timeTitle.text =
            requireContext().resources.getString(R.string.time)
    }

    override fun onDestroy() {
        pairListingAndDetailViewModel.unsubscribeFromALL()
        super.onDestroy()
    }

    private fun showPairList(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val inflater: LayoutInflater =
            LayoutInflater.from(activity).context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val dialogViewBinding: DialogPairListViewBinding =
            DataBindingUtil.inflate(
                inflater, R.layout.dialog_pair_list_view, null, false
            )

        dialogViewBinding.viewModel = pairListingAndDetailViewModel
        dialogViewBinding.lifecycleOwner = viewLifecycleOwner
        dialogViewBinding.selectorRv.adapter = adapter
        dialogViewBinding.selectorRv.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter.addData(pairListingAndDetailViewModel.allPairItem)

        adapter.clickListener.onItemClick = {

            pairListingAndDetailViewModel.subscribeFlow(it)
            checkVisibilityOfTradesRecyclerView()
            dialog.dismiss()
        }

        dialog.setContentView(dialogViewBinding.root)
        dialogViewBinding.cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
