package com.pedfu.daystreak.presentation.home.dialogs.confirmDeleteCategory

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentCategoryCreationDialogBinding
import com.pedfu.daystreak.databinding.FragmentConfirmDeleteDialogBinding
import com.pedfu.daystreak.presentation.reusable.showErrorModalSnackbar
import com.pedfu.daystreak.utils.lazyViewModel

enum class ConfirmDeleteType {
    CATEGORY,
    STREAK
}

class ConfirmDeleteDialogFragment(
    id: Long,
    private val type: ConfirmDeleteType = ConfirmDeleteType.CATEGORY
) : DialogFragment() {
    private var _binding: FragmentConfirmDeleteDialogBinding? = null
    private val binding: FragmentConfirmDeleteDialogBinding get() = _binding!!

    private val viewModel by lazyViewModel {
        ConfirmDeleteViewModel(id, type)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfirmDeleteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.run {
            buttonClose.setOnClickListener { dismiss() }
            buttonCancel.setOnClickListener { dismiss() }
            buttonDelete.setOnClickListener { viewModel.onDelete() }
            editTextName.addTextChangedListener { viewModel.onTypeChanged(it.toString()) }

            if (type == ConfirmDeleteType.STREAK) {
                textViewTitle.text = getString(R.string.are_you_sure_want_to_delete_streak)
                textViewMessage.text = getString(R.string.by_deleting_streak_will_be_lost)
            }

            observeViewModel()
        }
    }

    private fun FragmentConfirmDeleteDialogBinding.observeViewModel() {
        viewModel.categoryLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                val confirmationText = getString(R.string.type_the_category_name_to_confirm, it.name)
                val spannableString = SpannableString(confirmationText)
                val start = confirmationText.indexOf(it.name)
                val end = start + it.name.length
                spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                labelCategoryName.text = spannableString
            }
        }
        viewModel.streakLiveData.observe(viewLifecycleOwner) {
            labelCategoryName.isVisible = it?.isNotEmpty() == true
            textInputCategoryName.isVisible = it?.isNotEmpty() == true
        }
        viewModel.singleStreakLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                labelCategoryName.isVisible = false
                textInputCategoryName.isVisible = false
            }
        }
        viewModel.stateLiveData.observe(viewLifecycleOwner) { setState(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            if (it == true) {
                val rootView = requireActivity().findViewById<View>(android.R.id.content)
                showErrorModalSnackbar(rootView, R.string.network_error)
            }
        }
    }

    private fun FragmentConfirmDeleteDialogBinding.setState(state: ConfirmDeleteState) {
        when (state) {
            ConfirmDeleteState.IDLE -> {
                buttonDelete.isEnabled = false
                buttonDelete.isLoading = false
            }
            ConfirmDeleteState.READY -> {
                buttonDelete.isEnabled = true
                buttonDelete.isLoading = false
            }
            ConfirmDeleteState.DONE -> dismiss()
            ConfirmDeleteState.LOADING -> {
                buttonDelete.isEnabled = false
                buttonDelete.isLoading = true
            }
        }
    }
}