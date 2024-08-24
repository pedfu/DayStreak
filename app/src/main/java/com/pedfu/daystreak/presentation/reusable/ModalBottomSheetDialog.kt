package com.pedfu.daystreak.presentation.reusable

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.ItemBottomSheetBinding
import com.pedfu.daystreak.presentation.creation.streak.backgroundOptions.BackgroundOption
import com.pedfu.daystreak.presentation.creation.streak.backgroundOptions.BackgroundOptionsAdapter
import com.pedfu.daystreak.utils.BACKGROUND_OPTIONS
import com.pedfu.daystreak.utils.ImageProvider

class ModalBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: ItemBottomSheetBinding

    private lateinit var backgroundOptionsAdapter: BackgroundOptionsAdapter

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
            backgroundOptionsAdapter = BackgroundOptionsAdapter(requireContext())
            recyclerView.adapter = backgroundOptionsAdapter

            val options = BACKGROUND_OPTIONS.map { option ->
                BackgroundOption(option, ImageProvider.loadLocalImage(option, requireContext()))
            }
            backgroundOptionsAdapter.items = options
        }

        dialog?.setOnShowListener { it ->
//            val d = it as BottomSheetDialog
//            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//            bottomSheet?.let {
//                val behavior = BottomSheetBehavior.from(it)
//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
//            }


        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        const val TAG = "ModalBottomSheetDialog"
    }
}