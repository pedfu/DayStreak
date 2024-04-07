package com.pedfu.daystreak.presentation.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.pedfu.daystreak.presentation.MainActivity
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentHomeBinding
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.domain.streak.StreakStatus
import com.pedfu.daystreak.domain.user.User
import com.pedfu.daystreak.presentation.MainState
import com.pedfu.daystreak.presentation.MainViewModel
import com.pedfu.daystreak.presentation.detail.StreakDetailFragmentArgs
import com.pedfu.daystreak.presentation.home.adapters.NotificationAdapter
import com.pedfu.daystreak.presentation.home.adapters.StreakCategoryAdapter
import com.pedfu.daystreak.presentation.home.adapters.StreakListAdapter
import com.pedfu.daystreak.utils.Modals
import com.pedfu.daystreak.utils.lazyViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private val viewModel: HomeViewModel by lazyViewModel {
        HomeViewModel()
    }
    private val mainViewModel: MainViewModel by viewModels()

    private val adapter = StreakListAdapter(::navigateToDetails)
    private val categoryAdapter = StreakCategoryAdapter(::onSelectCategory)
    private val notificationAdapter = NotificationAdapter(::handleNotificationClick, ::handleShareClick, ::handleShareClick)

    private val listOfItems = null

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
        viewModel.streaksLiveData.observe(viewLifecycleOwner) { focusOnSelectedCategory() }
        viewModel.categoriesLiveData.observe(viewLifecycleOwner) { focusOnSelectedCategory(it) }
        viewModel.selectedCategoryLiveData.observe(viewLifecycleOwner) { focusOnSelectedCategory() }
        viewModel.notificationsLiveData.observe(viewLifecycleOwner) {
            notificationAdapter.items = it
        }
    }

    private fun FragmentHomeBinding.setUser(user: User?) {
        if (user != null) {
            Glide.with(requireContext())
                .load(user.photoUrl)
                .into(imageViewProfilePicture)

            startMainText.text = getString(R.string.hi_username, user.username)
        }
    }

    private fun FragmentHomeBinding.setState(state: MainState) {
        swipeRefreshLayout.isRefreshing = state == MainState.REFRESHING_SWIPE
    }

    private fun focusOnSelectedCategory(categoriesP: List<StreakCategoryItem>? = null) {
        val categories = categoriesP ?: viewModel.categoriesLiveData.value ?: emptyList()
        if (categories.isEmpty()) return
        val selectedCategory = categories.find { it.id == viewModel.selectedCategoryLiveData.value } ?: categories.first()
        categoryAdapter.items = categories.map {
            it.selected = it.id == selectedCategory.id
            it

        }
        adapter.items = (viewModel.streaksLiveData.value ?: emptyList()).filter { it.categoryId == selectedCategory.id }
    }

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
            showCreateCategoryDialog()
        }
        imageViewProfilePicture.setOnClickListener {
            (activity as? MainActivity)?.signOutAndStartSignInActivity()
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
        var selectedCategory: StreakCategoryItem? = null
        categoryAdapter.items = categoryAdapter.items.map {
            if (it.id == id) selectedCategory = it
            it.selected = it.id == id
            it
        }

        // update adapter items
        adapter.items = (viewModel.streaksLiveData.value ?: emptyList())
            .filter {
                it.categoryId == selectedCategory?.id
            }
    }

    private fun showNotificationsDialog() {
        Modals.showNotificationDialog(requireContext(), notificationAdapter.items, notificationAdapter)
    }

    private fun showCreateCategoryDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setupStreakForm(dialog)
        dialog.show()
    }

    private fun setupStreakForm(dialog: Dialog) {
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
            setupStreakInviteForm(dialog)
        }

        // add listeners
        val editTextName = dialog.findViewById<TextInputEditText>(R.id.editTextName)
        val editTextStreakNumber = dialog.findViewById<TextInputEditText>(R.id.editTextStreakNumber)
        val editTextStreakType = dialog.findViewById<TextInputEditText>(R.id.editTextStreakType)
        val editTextMinTimePerDay = dialog.findViewById<TextInputEditText>(R.id.editTextMinTimePerDay)
        val editTextCategory = dialog.findViewById<TextInputEditText>(R.id.editTextCategory)
        val editTextDescription = dialog.findViewById<TextInputEditText>(R.id.editTextDescription)
        val constraintLayoutBackgroundPicture = dialog.findViewById<ConstraintLayout>(R.id.constraintLayoutBackgroundPicture)

        editTextName.addTextChangedListener {  }
        editTextStreakNumber.addTextChangedListener {  }
        editTextStreakType.addTextChangedListener {  }
        editTextMinTimePerDay.addTextChangedListener {  }
        editTextCategory.addTextChangedListener {  }
        editTextDescription.addTextChangedListener {  }
        constraintLayoutBackgroundPicture.setOnClickListener {  }
    }

    private fun setupStreakInviteForm(dialog: Dialog) {
        dialog.setContentView(R.layout.dialog_streak_form_invite)

        val buttonClose = dialog.findViewById<ImageButton>(R.id.buttonClose)
        val buttonBack = dialog.findViewById<MaterialButton>(R.id.buttonBack)
        val buttonCreate = dialog.findViewById<MaterialButton>(R.id.buttonCreate)
        val editTextEmail = dialog.findViewById<TextInputEditText>(R.id.editTextEmail)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewPeople)

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        recyclerView.adapter = adapter
        ViewCompat.setNestedScrollingEnabled(recyclerView, true)

        buttonBack.setOnClickListener {
            setupStreakForm(dialog)
        }
        buttonCreate.setOnClickListener {
            dialog.hide()
        }
        buttonClose.setOnClickListener {
            // clear data in view model
            dialog.hide()
        }
        editTextEmail.addTextChangedListener {  }
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
}