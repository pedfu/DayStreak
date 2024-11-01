package com.pedfu.daystreak.presentation.reusable.localBackgroundBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.ItemBottomSheetBinding
import com.pedfu.daystreak.presentation.creation.streak.backgroundOptions.BackgroundOptionsAdapter
import com.pedfu.daystreak.utils.lazyViewModel

class ModalBottomSheetDialog(
    private val onSelectImage: (String, Int) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: ItemBottomSheetBinding

    private lateinit var backgroundOptionsAdapter: BackgroundOptionsAdapter
    private val viewModel by lazyViewModel {
        ModalLocalBackgroundBottomSheetViewModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView?.let {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            backgroundOptionsAdapter = BackgroundOptionsAdapter(requireContext(), ::onSelectImageAndDismiss)
            recyclerView.adapter = backgroundOptionsAdapter
        }

        binding.run {
            observeViewModel()
        }
    }

    private fun ItemBottomSheetBinding.observeViewModel() {
        viewModel.backgroundOptionsLiveData.observe(viewLifecycleOwner) {
            backgroundOptionsAdapter.items = it
            progressBar.isVisible = false
        }
    }

    private fun onSelectImageAndDismiss(name: String, imageRes: Int) {
        onSelectImage(name, imageRes)
        dismiss()
    }

    companion object {
        const val TAG = "ModalBottomSheetDialog"
    }
}