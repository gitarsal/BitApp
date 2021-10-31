package com.project.bitapp.presentation.pairlisting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.project.bitapp.R
import com.project.bitapp.databinding.FragmentPairListBinding
import com.project.bitapp.domain.model.AllPairItem
import com.project.bitapp.presentation.viewmodel.PairListingAndDetailViewModel
import com.project.bitapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PairListingFragment : Fragment() {
    private val pairListingAndDetailViewModel: PairListingAndDetailViewModel by activityViewModels()
    private var _binding: FragmentPairListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var adapter: PairListingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pair_list, container, false
        )

        binding.viewModel = pairListingAndDetailViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        adapter.clickListener.onItemClick = {
            pairListingAndDetailViewModel.subscribeFlow(it)
            findNavController().navigate(
                PairListingFragmentDirections.actionPairListToPairDetail()
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (pairListingAndDetailViewModel.allPairItem.isEmpty())
            getAllPairItems()
    }

    private fun getAllPairItems() {

        pairListingAndDetailViewModel.fetchPairList().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {

                    it.data?.let { currencyData ->
                        renderList(currencyData)
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun renderList(updatedList: List<AllPairItem>) {
        adapter.apply {
            addData(updatedList)
            pairListingAndDetailViewModel.allPairItem = updatedList
        }
    }
}
