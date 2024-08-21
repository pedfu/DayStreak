package com.pedfu.daystreak.presentation.creation.streak

import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentStreakCreationDialogBinding
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.presentation.creation.OnItemCreatedListener
import com.pedfu.daystreak.presentation.reusable.showErrorSnackbar
import com.pedfu.daystreak.utils.lazyViewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val REQUEST_IMAGE_SELECTED = 1

class StreakCreationDialogFragment(
    val dismissParent: () -> Unit
) : DialogFragment() {
    private var onItemCreatedListener: OnItemCreatedListener? = null
    fun setOnItemCreatedListener(listener: OnItemCreatedListener) {
        this.onItemCreatedListener = listener
    }

    private var _binding: FragmentStreakCreationDialogBinding? = null
    private val binding: FragmentStreakCreationDialogBinding get() = _binding!!

    private val calendar = Calendar.getInstance()

    private val viewModel: StreakCreationDialogViewModel by lazyViewModel {
        StreakCreationDialogViewModel()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStreakCreationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.run {
            buttonCreate.setOnClickListener {
                viewModel.onFinish()
            }
            buttonClose.setOnClickListener {
                dismissParent()
                dismiss()
                viewModel.resetData()
            }
            buttonCancel.setOnClickListener {
                dismiss()
                viewModel.resetData()
            }

            setupInputs()
            observeViewModel()
        }
    }

    private fun FragmentStreakCreationDialogBinding.observeViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { setState(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { setError(it) }
        viewModel.categoriesLiveData.observe(viewLifecycleOwner) { setCategoriesSpinner(it) }
        viewModel.streakCategoryLiveData.observe(viewLifecycleOwner) {
//            spinnerCategory.setTextext = it.name
        }
    }

    private fun FragmentStreakCreationDialogBinding.setupInputs() {
        editTextName.addTextChangedListener { viewModel.onStreakNameChanged(it.toString()) }
        editTextStreakMinPerDay.addTextChangedListener {
            val value = if (it.toString().isNullOrBlank()) "0" else it.toString()
            viewModel.onStreakMinTimePerDayInMinutesChanged(value.toInt())
        }
        editTextDescription.addTextChangedListener { viewModel.onStreakDescriptionChanged(it.toString()) }
        constraintLayoutBackgroundPicture.setOnClickListener { dispatchSelectPictureIntent() }
        imageViewPictureTaken.setOnClickListener { dispatchSelectPictureIntent() }
        textInputStreakGoalDeadline.setOnClickListener {
            showDatePicker(textInputStreakGoalDeadline)
        }
//        constraintLayoutBackgroundColor.setOnClickListener { showColorPicker() } precisa finalizar
    }

    private fun FragmentStreakCreationDialogBinding.setCategoriesSpinner(categories: List<StreakCategoryItem>) {
        // new streak - categories
        spinnerCategory.adapter = ArrayAdapter(requireContext(), R.layout.category_spinner_item, R.id.textViewOption, categories.map { it.name })
        spinnerCategory?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.onStreakCategoryChanged(categories[position])
            }
        }
    }

    private fun FragmentStreakCreationDialogBinding.setState(state: StreakCreationState) {
        when (state) {
            StreakCreationState.READY -> {
                buttonCreate.isLoading = false
                buttonCreate.isEnabled = true
            }
            StreakCreationState.DONE -> {
                buttonCreate.isLoading = false
                onItemCreatedListener?.onItemCreated("category")
                viewModel.resetData()
                dismissParent()
                dismiss()
            }
            StreakCreationState.LOADING -> {
                buttonCreate.isLoading = true
                buttonCreate.isEnabled = false
            }
            else -> {
                buttonCreate.isLoading = false
                buttonCreate.isEnabled = false
            }
        }
    }

    private fun FragmentStreakCreationDialogBinding.setError(errors: List<StreakCreationFields>) {
        when {
            errors.contains(StreakCreationFields.NAME) -> {
//                textViewErrorName.text = getString(R.string.name_cannot_be_empty)
//                textViewErrorName.isVisible = true
            }
            errors.contains(StreakCreationFields.NETWORK) -> {
                val rootView = requireActivity().findViewById<View>(android.R.id.content)
                showErrorSnackbar(rootView, R.string.network_error)
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
                viewModel.onStreakGoalDeadlineChanged(Date(formattedDate))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        // Show the DatePicker dialog
        datePickerDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_SELECTED -> {
                val imageUri = data?.data
                imageUri?.let { uri ->
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val outputStream = FileOutputStream(File(requireContext().cacheDir, "selected_image.jpg"))
                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }

                    val file = File(requireContext().cacheDir, "selected_image.jpg")
                    viewModel.onStreakBackgroundImageChanged(file)

                    val bitmap = BitmapFactory.decodeFile(file.path)
                    val constraintLayout = dialog?.findViewById<ConstraintLayout>(R.id.constraintLayoutBackgroundPicture)
                    val imageView = dialog?.findViewById<ImageView>(R.id.imageViewPictureTaken)
                    imageView?.setImageBitmap(bitmap)
                    constraintLayout?.isVisible = false
                    imageView?.isVisible = true
                }
            }
        }
    }

    private fun dispatchSelectPictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_PICK_IMAGES)
        takePictureIntent.type = "image/*"
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_SELECTED)
        } catch (e: ActivityNotFoundException) {
            // error
        }
    }
}