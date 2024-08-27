package com.pedfu.daystreak.presentation.home

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentHomeBinding
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.domain.user.User
import com.pedfu.daystreak.presentation.MainActivity
import com.pedfu.daystreak.presentation.MainState
import com.pedfu.daystreak.presentation.MainViewModel
import com.pedfu.daystreak.presentation.creation.OnItemCreatedListener
import com.pedfu.daystreak.presentation.creation.SelectCreateTypeDialogFragment
import com.pedfu.daystreak.presentation.detail.StreakDetailFragmentArgs
import com.pedfu.daystreak.presentation.header.notification.NotificationDialogFragment
import com.pedfu.daystreak.presentation.home.adapters.NotificationAdapter
import com.pedfu.daystreak.presentation.home.adapters.StreakCategoryAdapter
import com.pedfu.daystreak.presentation.home.adapters.StreakListAdapter
import com.pedfu.daystreak.presentation.home.dialogs.confirmDeleteCategory.ConfirmDeleteDialogFragment
import com.pedfu.daystreak.presentation.home.dialogs.confirmDeleteCategory.ConfirmDeleteType
import com.pedfu.daystreak.presentation.reusable.localBackgroundBottomSheet.ModalBottomSheetDialog
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
        HomeViewModel(requireContext())
    }
    private val mainViewModel: MainViewModel by viewModels()

    private val adapter = StreakListAdapter(::navigateToDetails, ::showPopupMenu)
    private val categoryAdapter = StreakCategoryAdapter(::onSelectCategory, ::showPopupMenu)
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
            textViewNoStreaks.isVisible = it.isEmpty()
        }
        viewModel.notificationsLiveData.observe(viewLifecycleOwner) { setNotifications(it) }
    }

    private fun setNotifications(notifications: List<NotificationItem>) {
        val textView = view?.findViewById<TextView>(R.id.textViewNotificationQnt)
        if (textView != null) {
            textView.text = notifications.size.toString()
        }
    }

    private fun FragmentHomeBinding.setUser(user: User?) {
        if (user != null) {
            val imageViewProfilePicture = view?.findViewById<ShapeableImageView>(R.id.imageViewProfilePicture)
            if (imageViewProfilePicture != null) {
                Glide.with(requireContext())
                    .load(user.photoUrl)
                    .into(imageViewProfilePicture)
            }


            startMainText.text = getString(R.string.hi_username, user.username)
            dayStreakQnt.text = user.maxStreak.toString()
        }
    }

    private fun FragmentHomeBinding.setState(state: MainState) {
        swipeRefreshLayout.isRefreshing = state == MainState.REFRESHING_SWIPE
        progressBar.isVisible = state == MainState.REFRESHING
    }

    private fun FragmentHomeBinding.setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            mainViewModel.refresh(true)
        }
    }

    private fun FragmentHomeBinding.setupButtons() {
        view?.findViewById<ConstraintLayout>(R.id.frameLayoutBell)?.setOnClickListener {
            val notificationDialog = NotificationDialogFragment()
            notificationDialog.show(childFragmentManager, "NotificationModalDialog")
        }
        view?.findViewById<ShapeableImageView>(R.id.imageViewProfilePicture)?.setOnClickListener {
            (activity as? MainActivity)?.signOutAndStartSignInActivity()
        }

        createStreakCategoryButton.setOnClickListener {
            val createItemDialog = SelectCreateTypeDialogFragment()
            createItemDialog.setOnItemCreatedListener(object : OnItemCreatedListener {
                override fun onItemCreated(itemType: String) {
                    Toast.makeText(requireContext(), "$itemType created!", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            createItemDialog.show(childFragmentManager, "CreateItemDialog")
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
//            setupCreateStreakForm()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_or_delete, menu)

        // Customize each menu item
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val customView = LayoutInflater.from(requireContext()).inflate(R.layout.menu_item_layout, null)
            val textView = customView.findViewById<TextView>(R.id.menu_item_text)

            // Set the menu title text
            textView.text = menuItem.title

            // Apply custom color based on the menu item ID
            when (menuItem.itemId) {
                R.id.edit -> textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_font))
                R.id.delete -> textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_font))
            }

            menuItem.actionView = customView
        }
    }

    private fun showPopupMenu(view: View, id: Long, type: ConfirmDeleteType = ConfirmDeleteType.CATEGORY) {
        val popupMenu = PopupMenu(requireContext(), view)

        if (type == ConfirmDeleteType.STREAK) {
            popupMenu.menuInflater.inflate(R.menu.menu_delete, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        val modal = ConfirmDeleteDialogFragment(id, ConfirmDeleteType.STREAK)
                        modal.show(parentFragmentManager, "ConfirmDeleteDialogFragment")
                        true
                    }
                    else -> false
                }
            }
        } else {
            popupMenu.menuInflater.inflate(R.menu.menu_edit_or_delete, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.edit -> {
                        Toast.makeText(requireContext(), "Editado", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.delete -> {
                        val modal = ConfirmDeleteDialogFragment(id)
                        modal.show(parentFragmentManager, "ConfirmDeleteDialogFragment")
                        true
                    }
                    else -> false
                }
            }
        }
        popupMenu.show()
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