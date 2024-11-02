package com.pedfu.daystreak.presentation.creation.completeDay

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentCategoryCreationDialogBinding
import com.pedfu.daystreak.databinding.FragmentCompleteDayCreationDialogBinding
import com.pedfu.daystreak.presentation.creation.OnItemCreatedListener
import com.pedfu.daystreak.presentation.reusable.showErrorModalSnackbar
import com.pedfu.daystreak.utils.lazyViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CompleteDayCreationDialogFragment(
    private val id: Long
) : DialogFragment() {
    private var onItemCreatedListener: OnItemCreatedListener? = null
    fun setOnItemCreatedListener(listener: OnItemCreatedListener) {
        this.onItemCreatedListener = listener
    }

    private var _binding: FragmentCompleteDayCreationDialogBinding? = null
    private val binding: FragmentCompleteDayCreationDialogBinding get() = _binding!!

    private val viewModel: CompleteDayCreationDialogViewModel by lazyViewModel {
        CompleteDayCreationDialogViewModel(id)
    }

    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompleteDayCreationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.run {
            buttonSave.setOnClickListener {
                viewModel.onFinish()
            }
            buttonClose.setOnClickListener {
                dismiss()
                viewModel.resetData()
            }
            buttonCancel.setOnClickListener {
                dismiss()
                viewModel.resetData()
            }

            buttonInputHour.setOnClickListener {
                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    buttonInputHour.setText(String.format("%02d:%02d", hour, minute))
                    val minutes = (hour * 60) + minute // to minutes
                    viewModel.onStreakDayTimeInMinutesChanged(minutes)
                }
                val currentSavedMinutes = viewModel.streakDayTimeInMinutesLiveData.value ?: 0
                val currHours = (currentSavedMinutes / 60).toInt()
                val currMin = currentSavedMinutes % 60
                TimePickerDialog(requireContext(), timeSetListener, currHours, currMin, true).show()
            }

            editTextDescription.addTextChangedListener { viewModel.onStreakDescriptionChanged(it.toString()) }
            buttonInputDate.setOnClickListener {
                showDatePicker(buttonInputDate)
            }

            observeViewModel()
        }
    }

    private fun FragmentCompleteDayCreationDialogBinding.observeViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { setState(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { setError(it) }
    }

    private fun FragmentCompleteDayCreationDialogBinding.setState(state: CompleteDayCreationState) {
        when (state) {
            CompleteDayCreationState.READY -> {
                buttonSave.isLoading = false
                buttonSave.isEnabled = true
            }
            CompleteDayCreationState.DONE -> {
                buttonSave.isLoading = false
                onItemCreatedListener?.onItemCreated("category")
                viewModel.resetData()
                dismiss()
            }
            CompleteDayCreationState.LOADING -> {
                buttonSave.isLoading = true
                buttonSave.isEnabled = false
            }
            else -> {
                buttonSave.isLoading = false
                buttonSave.isEnabled = false
            }
        }
    }

    private fun FragmentCompleteDayCreationDialogBinding.setError(error: String?) {
        when (error) {
            EMPTY_NAME -> {
//                textViewErrorName.text = getString(R.string.name_cannot_be_empty)
//                textViewErrorName.isVisible = true
            }
            EXISTING_NAME -> {
//                textViewErrorName.text = getString(R.string.category_name_already_exists)
//                textViewErrorName.isVisible = true
            }
            NETWORK -> {
                val rootView = requireActivity().findViewById<View>(android.R.id.content)
                showErrorModalSnackbar(rootView, R.string.network_error)
            }
            else -> {
//                textViewErrorName.isVisible = false
            }
        }
    }

    private fun showDatePicker(textView: AppCompatButton) {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this.requireContext(), {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                textView.text = "$formattedDate"
                viewModel.onStreakDateChanged(Date(formattedDate))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        // Show the DatePicker dialog
        datePickerDialog.show()
    }
}