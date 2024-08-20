package com.pedfu.daystreak.presentation.category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentCategoryCreationDialogBinding
import com.pedfu.daystreak.databinding.FragmentHomeBinding
import com.pedfu.daystreak.presentation.MainViewModel
import com.pedfu.daystreak.presentation.home.HomeViewModel
import com.pedfu.daystreak.utils.lazyViewModel

class CategoryCreationDialogFragment(
    val dismissParent: () -> Unit
) : DialogFragment() {
    private var onItemCreatedListener: OnItemCreatedListener? = null
    fun setOnItemCreatedListener(listener: OnItemCreatedListener) {
        this.onItemCreatedListener = listener
    }

    private var _binding: FragmentCategoryCreationDialogBinding? = null
    private val binding: FragmentCategoryCreationDialogBinding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>(::requireActivity)
    private val viewModel: CategoryCreationDialogViewModel by lazyViewModel {
        CategoryCreationDialogViewModel(homeViewModel)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryCreationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        binding.run {
            buttonCreate.setOnClickListener {
                viewModel.onFinish()
            }
            buttonClose.setOnClickListener {
                dismissParent()
                dismiss()
                viewModel.resetData()
            }
            buttonBack.setOnClickListener {
                dismiss()
                viewModel.resetData()
            }
            editTextName.addTextChangedListener { viewModel.onCategoryNameChanged(it.toString()) }

            observeViewModel()
        }
    }

    private fun FragmentCategoryCreationDialogBinding.observeViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { setState(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { setError(it) }
    }

    private fun FragmentCategoryCreationDialogBinding.setState(state: CategoryCreationState) {
        when (state) {
            CategoryCreationState.READY -> {
                buttonCreate.isLoading = false
                buttonCreate.isEnabled = true
            }
            CategoryCreationState.DONE -> {
                buttonCreate.isLoading = false
                onItemCreatedListener?.onItemCreated("category")
                viewModel.resetData()
                dismissParent()
                dismiss()
            }
            CategoryCreationState.LOADING -> {
                buttonCreate.isLoading = true
                buttonCreate.isEnabled = false
            }
            else -> {
                buttonCreate.isLoading = false
                buttonCreate.isEnabled = false
            }
        }
    }

    private fun FragmentCategoryCreationDialogBinding.setError(error: String?) {
        when (error) {
            EMPTY_NAME -> {
                textViewErrorName.text = getString(R.string.name_cannot_be_empty)
                textViewErrorName.isVisible = true
            }
            EXISTING_NAME -> {
                textViewErrorName.text = getString(R.string.category_name_already_exists)
                textViewErrorName.isVisible = true
            }
            else -> {
                textViewErrorName.isVisible = false
            }
        }
    }
}