package com.pedfu.daystreak.presentation.creation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentSelectCreateTypeDialogBinding
import com.pedfu.daystreak.presentation.MainViewModel
import com.pedfu.daystreak.presentation.creation.category.CategoryCreationDialogFragment
import com.pedfu.daystreak.presentation.creation.streak.StreakCreationDialogFragment
import com.pedfu.daystreak.presentation.home.HomeViewModel
import com.pedfu.daystreak.utils.lazyViewModel

class SelectCreateTypeDialogFragment : DialogFragment() {
    private var onItemCreatedListener: OnItemCreatedListener? = null
    fun setOnItemCreatedListener(listener: OnItemCreatedListener) {
        this.onItemCreatedListener = listener
    }

    private var _binding: FragmentSelectCreateTypeDialogBinding? = null
    private val binding: FragmentSelectCreateTypeDialogBinding get() = _binding!!

    private val viewModel: SelectCreateTypeViewModel by lazyViewModel { SelectCreateTypeViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectCreateTypeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        binding.run {
            viewModel.categoriesLiveData.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    buttonStreak.setTextAppearance(R.style.MaterialButton_Disabled)
                    buttonStreak.isEnabled = false
                } else {
                    buttonStreak.setTextAppearance(R.style.MaterialButton)
                    buttonStreak.isEnabled = true
                }
            }

            buttonCategory.setOnClickListener {
                showCategoryCreationModal()
            }
            buttonStreak.setOnClickListener {
                showStreakCreationModal()
            }
            buttonClose.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun showCategoryCreationModal() {
        val categoryCreationDialog = CategoryCreationDialogFragment(::dismiss)
        categoryCreationDialog.setOnItemCreatedListener(object : OnItemCreatedListener {
            override fun onItemCreated(itemType: String) {
                onItemCreatedListener?.onItemCreated(itemType)
                dismiss()
            }
        })
        categoryCreationDialog.show(childFragmentManager, "CreateCategoryDialog")
    }

    private fun showStreakCreationModal() {
        val steakCreationDialog = StreakCreationDialogFragment(::dismiss)
        steakCreationDialog.setOnItemCreatedListener(object : OnItemCreatedListener {
            override fun onItemCreated(itemType: String) {
                onItemCreatedListener?.onItemCreated(itemType)
                dismiss()
            }
        })
        steakCreationDialog.show(childFragmentManager, "CreateStreakDialog")
    }
}