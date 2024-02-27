package com.pedfu.daystreak.presentation.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentHomeBinding
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.domain.streak.StreakStatus
import com.pedfu.daystreak.presentation.detail.StreakDetailFragmentArgs
import com.pedfu.daystreak.presentation.home.adapters.NotificationAdapter
import com.pedfu.daystreak.presentation.home.adapters.StreakCategoryAdapter
import com.pedfu.daystreak.presentation.home.adapters.StreakListAdapter
import com.pedfu.daystreak.presentation.utils.Modals

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private val adapter = StreakListAdapter(::navigateToDetails)
    private val categoryAdapter = StreakCategoryAdapter(::onSelectCategory)
    private val notificationAdapter = NotificationAdapter(::handleNotificationClick, ::handleShareClick, ::handleShareClick)

    private val listOfItems = listOf(StreakItem(1, "", "Test titulo", "Teste descricao", 1, StreakStatus.PENDING),
        StreakItem(2, "", "Test titulo 2", "Teste descricao", 2, StreakStatus.PENDING),
        StreakItem(3, "", "Test titulo3", "Teste descricao", 3, StreakStatus.DAY_DONE),
        StreakItem(4, "", "Test titulo4", "Teste descricao", 2, StreakStatus.STREAK_OVER),
        StreakItem(5, "", "Test titulo5", "Teste descricao", 2, StreakStatus.PENDING),
        StreakItem(6, "", "Test titulo6", "Teste descricao", 1, StreakStatus.PENDING),
        StreakItem(7, "", "Test titulo7", "Teste descricao", 4, StreakStatus.PENDING),
        StreakItem(8, "", "Test titulo8", "Teste descricao", 1, StreakStatus.PENDING),
        StreakItem(9, "", "Test titulo9", "Teste descricao", 3, StreakStatus.PENDING),
        StreakItem(10, "", "Test titulo10", "Teste descricao", 2, StreakStatus.PENDING),
        StreakItem(11, "", "Test titulo11", "Teste descricao", 2, StreakStatus.PENDING)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            setupButtons()
            setupRecyclerView()
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

    private fun FragmentHomeBinding.setupButtons() {
        frameLayoutBell.setOnClickListener {
            showNotificationsDialog()
        }
        createStreakCategoryButton.setOnClickListener {
            showCreateCategoryDialog()
        }
    }

    private fun FragmentHomeBinding.setupRecyclerView() {
        // categories
        recyclerViewStreakCategories.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recyclerViewStreakCategories.adapter = categoryAdapter
        ViewCompat.setNestedScrollingEnabled(recyclerViewStreakCategories, true)
        categoryAdapter.items = listOf(
            StreakCategoryItem(1, "Main", true),
            StreakCategoryItem(2, "Trabalho", false),
            StreakCategoryItem(3, "Estudos", false),
            StreakCategoryItem(4, "Internet", false),
            StreakCategoryItem(5, "Hobbies", false),
        )

        // streak items
        val selectedCategory = categoryAdapter.items.find { it.selected }
        recyclerViewStreaks.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recyclerViewStreaks.adapter = adapter
        ViewCompat.setNestedScrollingEnabled(recyclerViewStreaks, true)

        adapter.items = listOfItems.filter { it.categoryId == selectedCategory?.id }
    }

    private fun onSelectCategory(id: Long) {
        var selectedCategory: StreakCategoryItem? = null
        categoryAdapter.items = categoryAdapter.items.map {
            if (it.id == id) selectedCategory = it
            it.selected = it.id == id
            it
        }

        // update adapter items
        adapter.items = listOfItems.filter { it.categoryId == selectedCategory?.id }
    }

    private fun showNotificationsDialog() {
        val items = listOf(
            NotificationItem(1, "You got a 10 days streak badge in XXXX!", "", true, false),
            NotificationItem(2, "You got invited to a streak challenge in XXXX against Pedro!", "", false, true),
            NotificationItem(3, "You got a 10 days streak badge in XXXX!", "", true, false),
            NotificationItem(4, "You got invited to a streak challenge in XXXX against Pedro!", "", false, false),
            NotificationItem(5, "You got invited to a streak challenge in XXXX against Pedro!", "", true, true),
        )
        Modals.showNotificationDialog(requireContext(), items, notificationAdapter)
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
//        adapter.items = listOf(
//            StreakCategoryItem(1, "Main", true),
//            StreakCategoryItem(2, "Trabalho", false),
//            StreakCategoryItem(3, "Estudos", false),
//            StreakCategoryItem(4, "Internet", false),
//            StreakCategoryItem(5, "Hobbies", false),
//        )

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