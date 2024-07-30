package com.pedfu.daystreak.presentation.home

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentHomeBinding
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.domain.user.User
import com.pedfu.daystreak.presentation.MainActivity
import com.pedfu.daystreak.presentation.MainState
import com.pedfu.daystreak.presentation.MainViewModel
import com.pedfu.daystreak.presentation.detail.StreakDetailFragmentArgs
import com.pedfu.daystreak.presentation.home.adapters.NotificationAdapter
import com.pedfu.daystreak.presentation.home.adapters.StreakCategoryAdapter
import com.pedfu.daystreak.presentation.home.adapters.StreakListAdapter
import com.pedfu.daystreak.utils.Modals
import com.pedfu.daystreak.utils.lazyViewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val REQUEST_IMAGE_SELECTED = 1

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private lateinit var dialog: Dialog

    private val viewModel: HomeViewModel by lazyViewModel {
        HomeViewModel()
    }
    private val mainViewModel: MainViewModel by viewModels()

    private val adapter = StreakListAdapter(::navigateToDetails)
    private val categoryAdapter = StreakCategoryAdapter(::onSelectCategory)
    private val notificationAdapter = NotificationAdapter(::handleNotificationClick, ::handleShareClick, ::handleShareClick)
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = Dialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            setupSwipeRefresh()
            setupButtons()
            setupRecyclerView()
            observeViewModel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FragmentHomeBinding.observeViewModel() {
        mainViewModel.stateLiveData.observe(viewLifecycleOwner) { setState(it) }

        viewModel.userLiveData.observe(viewLifecycleOwner) { setUser(it) }
        viewModel.selectedCategoryLiveData.observe(viewLifecycleOwner) {
            categoryAdapter.items = categoryAdapter.items.map { item ->
                item.selected = item.id == it
                item
            }
            viewModel.filterStreaks()
        }
        viewModel.streaksLiveData.observe(viewLifecycleOwner) { viewModel.filterStreaks() }
        viewModel.categoriesLiveData.observe(viewLifecycleOwner) {
            var selectedId = viewModel.selectedCategoryLiveData.value
            if (selectedId == -1L && it.isNotEmpty()) {
                selectedId = it.first().id
                viewModel.onSelectCategory(selectedId)
            }
            categoryAdapter.items = it.map { item ->
                item.selected = item.id == selectedId
                item
            }
        }
        viewModel.filteredStreaksLiveData.observe(viewLifecycleOwner) {
            adapter.items = it
        }
        viewModel.notificationsLiveData.observe(viewLifecycleOwner) { setNotifications(it) }
    }

    private fun FragmentHomeBinding.setNotifications(notifications: List<NotificationItem>) {
        notificationAdapter.items = notifications
        textViewNotificationQnt.text = notifications.size.toString()
        textViewNotificationQnt.isVisible = notifications.isNotEmpty()
    }

    private fun FragmentHomeBinding.setUser(user: User?) {
        if (user != null) {
            Glide.with(requireContext())
                .load(user.photoUrl)
                .into(imageViewProfilePicture)

            startMainText.text = getString(R.string.hi_username, user.username)
            dayStreakQnt.text = user.maxStreak.toString()
        }
    }

    private fun FragmentHomeBinding.setState(state: MainState) {
        swipeRefreshLayout.isRefreshing = state == MainState.REFRESHING_SWIPE
        progressBar.isVisible = state == MainState.REFRESHING
    }

//    private fun focusOnSelectedCategory(categoriesP: List<StreakCategoryItem>? = null, streaksP: List<StreakItem>? = null) {
//        Log.i("ULTIMATETESTE", "categoria - " + categoriesP.toString() + "\n" + categoriesP?.size)
//        Log.i("ULTIMATETESTE", "categoria vm - " + viewModel.categoriesLiveData.value.toString() + "\n" + viewModel.categoriesLiveData.value?.size)
//        Log.i("ULTIMATETESTE", "streak - " + streaksP.toString() + "\n" + streaksP?.size)
//        Log.i("ULTIMATETESTE", "streak vm - " + viewModel.streaksLiveData.value.toString() + "\n" + viewModel.streaksLiveData.value?.size)
//        val categories = categoriesP ?: viewModel.categoriesLiveData.value ?: emptyList()
//        if (categories.isEmpty()) return
//        val selectedCategory = categories.find { it.id == viewModel.selectedCategoryLiveData.value } ?: categories.first()
//        categoryAdapter.items = categories.map {
//            it.selected = it.id == selectedCategory.id
//            it
//
//        }
//        Log.i("ULTIMATETESTE", "final vm - " + selectedCategory.toString() + "\n" + (streaksP ?: viewModel.streaksLiveData.value ?: emptyList()).filter { it.categoryId == selectedCategory.id }.toString())
//        adapter.items = (streaksP ?: viewModel.streaksLiveData.value ?: emptyList()).filter { it.categoryId == selectedCategory.id }
//    }

    private fun FragmentHomeBinding.setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            mainViewModel.refresh(true)
        }
    }

    private fun FragmentHomeBinding.setupButtons() {
        frameLayoutBell.setOnClickListener {
            showNotificationsDialog()
        }
        createStreakCategoryButton.setOnClickListener {
            selectOptionModal.isVisible = true
            selectOptionModal.layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        }
        imageViewProfilePicture.setOnClickListener {
            (activity as? MainActivity)?.signOutAndStartSignInActivity()
        }
        buttonStreak.setOnClickListener {
            selectOptionModal.isVisible = false
            showCreateCategoryDialog(false)
        }
        buttonCategory.setOnClickListener {
            selectOptionModal.isVisible = false
            showCreateCategoryDialog(true)
        }
        buttonClose.setOnClickListener {
            selectOptionModal.isVisible = false
            dialog.hide()
        }
        selectOptionModal.setOnClickListener {
            selectOptionModal.isVisible = false
        }
    }

    private fun FragmentHomeBinding.setupRecyclerView() {
        // categories
        recyclerViewStreakCategories.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recyclerViewStreakCategories.adapter = categoryAdapter
        ViewCompat.setNestedScrollingEnabled(recyclerViewStreakCategories, true)

        // streaks
        recyclerViewStreaks.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recyclerViewStreaks.adapter = adapter
        ViewCompat.setNestedScrollingEnabled(recyclerViewStreaks, true)
    }

    private fun onSelectCategory(id: Long) {
        viewModel.onSelectCategory(id)
    }

    private fun showNotificationsDialog() {
        Modals.showNotificationDialog(requireContext(), notificationAdapter.items, notificationAdapter)
    }

    private fun showCreateCategoryDialog(isCategory: Boolean) {
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        when (isCategory) {
            true -> setupCreateCategoryForm()
            else -> setupStreakForm()
        }
        dialog.show()
    }

    private fun setupStreakForm() {
        // dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_streak_form)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        val buttonClose = dialog.findViewById<ImageButton>(R.id.buttonClose)
        val buttonContinue = dialog.findViewById<MaterialButton>(R.id.buttonContinue)
        val buttonCancel = dialog.findViewById<MaterialButton>(R.id.buttonCancel)
        buttonClose.setOnClickListener {
            dialog.hide()
        }
        buttonCancel.setOnClickListener {
            dialog.hide()
        }
        buttonContinue.setOnClickListener {
            // change for second
            // setupStreakInviteForm() -> uncomment when implement invite
            viewModel.onCreateStreak(dialog::hide)
        }

        // add listeners
        val editTextName = dialog.findViewById<TextInputEditText>(R.id.editTextName)
        val textInputStreakGoalDeadline = dialog.findViewById<AppCompatButton>(R.id.textInputStreakGoalDeadline)
        val editTextMinTimePerDay = dialog.findViewById<TextInputEditText>(R.id.editTextStreakMinPerDay)
        val editTextCategory = dialog.findViewById<TextInputEditText>(R.id.editTextCategory)
        val editTextDescription = dialog.findViewById<TextInputEditText>(R.id.editTextDescription)
        val constraintLayoutBackgroundPicture = dialog.findViewById<ConstraintLayout>(R.id.constraintLayoutBackgroundPicture)
        val imageViewPictureTaken = dialog.findViewById<ImageView>(R.id.imageViewPictureTaken)
        val constraintLayoutBackgroundColor = dialog.findViewById<ConstraintLayout>(R.id.constraintLayoutBackgroundColor)

//        editTextStreakGoalDeadline.isEnabled = false
        textInputStreakGoalDeadline.setOnClickListener {
            Log.i("TESTE", "CLICADO")
            showDatePicker(textInputStreakGoalDeadline)
        }

        editTextName.addTextChangedListener { viewModel.onStreakNameChanged(it.toString()) }
        editTextMinTimePerDay.addTextChangedListener { viewModel.onStreakMinTimePerDayChanged(it.toString()) }
        editTextCategory.addTextChangedListener { viewModel.onStreakCategoryChanged(it.toString()) }
        editTextDescription.addTextChangedListener { viewModel.onStreakDescriptionChanged(it.toString()) }
        constraintLayoutBackgroundPicture.setOnClickListener { dispatchSelectPictureIntent() }
        imageViewPictureTaken.setOnClickListener { dispatchSelectPictureIntent() }
        constraintLayoutBackgroundColor.setOnClickListener { showColorPicker() }
    }

//    private fun setupStreakInviteForm() {
//        dialog.setContentView(R.layout.dialog_streak_form_invite)
//
//        val buttonClose = dialog.findViewById<ImageButton>(R.id.buttonClose)
//        val buttonBack = dialog.findViewById<MaterialButton>(R.id.buttonBack)
//        val buttonCreate = dialog.findViewById<MaterialButton>(R.id.buttonCreate)
//        val editTextEmail = dialog.findViewById<TextInputEditText>(R.id.editTextEmail)
//        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewPeople)
//
//        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        // recyclerView.adapter = adapter
//        ViewCompat.setNestedScrollingEnabled(recyclerView, true)
//
//        buttonBack.setOnClickListener {
//            setupStreakForm()
//        }
//        buttonCreate.setOnClickListener {
//            dialog.hide()
//        }
//        buttonClose.setOnClickListener {
//            // clear data in view model
//            dialog.hide()
//        }
//        editTextEmail.addTextChangedListener {  }
//    }

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
        // Show the DatePicker dialog
        datePickerDialog.show()
    }

    private fun showColorPicker() {
    }

    private fun setupCreateCategoryForm() {
//        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_category_form)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        val buttonClose = dialog.findViewById<ImageButton>(R.id.buttonClose)
        val buttonBack = dialog.findViewById<MaterialButton>(R.id.buttonBack)
        val buttonCreate = dialog.findViewById<MaterialButton>(R.id.buttonCreate)
        val editTextName = dialog.findViewById<TextInputEditText>(R.id.editTextName)

        buttonCreate.setOnClickListener {
            viewModel.onCreateCategory(dialog::hide)
        }
        buttonClose.setOnClickListener {
            // clear data in view model
            viewModel.onCategoryNameChanged("")
            dialog.hide()
        }
        buttonBack.setOnClickListener {
            // clear data in view model
            viewModel.onCategoryNameChanged("")
            dialog.hide()
        }
        editTextName.addTextChangedListener { viewModel.onCategoryNameChanged(it.toString()) }
    }

    private fun handleShareClick() {}
    private fun handleShareClick(id: Long) {}

    private fun handleNotificationClick(id: Long) {
        notificationAdapter.items = notificationAdapter.items.map {
            if (it.id == id) it.read = true
            it
        }
        Modals.showBadgeDialog(requireContext(), ::handleShareClick, "Streak Master", "Reach a 10-day streak. Teste.")
    }

    private fun navigateToDetails(streakId: Long) {
        val args = StreakDetailFragmentArgs.Builder(streakId).build()
        findNavController().navigate(R.id.action_from_home_to_details, args.toBundle())
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
                    viewModel.onStreakBackgroundPictureChanged(file)

                    val bitmap = BitmapFactory.decodeFile(file.path)
                    val constraintLayout = dialog.findViewById<ConstraintLayout>(R.id.constraintLayoutBackgroundPicture)
                    val imageView = dialog.findViewById<ImageView>(R.id.imageViewPictureTaken)
                    imageView?.setImageBitmap(bitmap)
                    constraintLayout.isVisible = false
                    imageView.isVisible = true
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