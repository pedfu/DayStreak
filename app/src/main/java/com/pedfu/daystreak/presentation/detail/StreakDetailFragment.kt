package com.pedfu.daystreak.presentation.detail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.imageview.ShapeableImageView
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentStreakDetailBinding
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.domain.streak.StreakStatus
import com.pedfu.daystreak.domain.user.User
import com.pedfu.daystreak.presentation.creation.OnItemCreatedListener
import com.pedfu.daystreak.presentation.creation.completeDay.CompleteDayCreationDialogFragment
import com.pedfu.daystreak.presentation.creation.streak.StreakCreationDialogFragment
import com.pedfu.daystreak.presentation.header.notification.NotificationDialogFragment
import com.pedfu.daystreak.presentation.home.adapters.NotificationAdapter
import com.pedfu.daystreak.presentation.timer.TimerFragmentArgs
import com.pedfu.daystreak.utils.ImageProvider
import com.pedfu.daystreak.utils.Modals
import com.pedfu.daystreak.utils.lazyViewModel

const val CANCEL = 1
const val HIDE = 2
const val DELETE = 3

class StreakDetailFragment : Fragment() {

    private var _binding: FragmentStreakDetailBinding? = null
    private val binding: FragmentStreakDetailBinding get() = _binding!!

    private val args by navArgs<StreakDetailFragmentArgs>()

    private val viewModel by lazyViewModel {
        StreakDetailViewModel(args.streakId)
    }

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private val notificationAdapter = NotificationAdapter(::handleNotificationClick, ::handleShareClick, ::handleShareClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()
        binding.run {
            observeViewModel()
            setupSwipeRefresh()
            setupButtons()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStreakDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBackButton() {
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigateUp()
        }
    }

    private fun FragmentStreakDetailBinding.setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshDetails()
        }
    }

    private fun FragmentStreakDetailBinding.observeViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { setState(it) }
        viewModel.streakLiveData.observe(viewLifecycleOwner) { setStreak(it) }
        viewModel.userLiveData.observe(viewLifecycleOwner) { setUser(it) }
        viewModel.notificationsLiveData.observe(viewLifecycleOwner) { setNotifications(it) }
    }

    private fun FragmentStreakDetailBinding.setupButtons() {
        notificationHeaderView.setShowNotificationsModal {
            val notificationDialog = NotificationDialogFragment()
            notificationDialog.show(childFragmentManager, "NotificationModalDialog")
        }
        notificationHeaderView.setNavigateToAction {
            findNavController().navigate(R.id.action_from_details_to_profile)
        }

        buttonStartTimer.setOnClickListener {
            // precisa aparecer modal -> pessoa digita quantidade -> inicia timer
            val args = TimerFragmentArgs.Builder(15).build()
            findNavController().navigate(R.id.action_from_details_to_timer, args.toBundle())
        }
        imageButtonDelete.setOnClickListener {
            showConfirmationAdvancedModal()
        }
        buttonCompleteDay.setOnClickListener {
            val completeDayDialog = CompleteDayCreationDialogFragment(args.streakId)
            completeDayDialog.setOnItemCreatedListener(object : OnItemCreatedListener {
                override fun onItemCreated(itemType: String) {
                    viewModel.refreshDetails()
                }
            })
            completeDayDialog.show(childFragmentManager, "CompleteDayCreationDialog")
        }
        buttonEdit.setOnClickListener {
            val steakEditDialog = StreakCreationDialogFragment(::close, args.streakId)
            steakEditDialog.setOnItemCreatedListener(object : OnItemCreatedListener {
                override fun onItemCreated(itemType: String) {
                    viewModel.refreshDetails()
                }
            })
            steakEditDialog.show(childFragmentManager, "EditStreakDialog")
        }
    }

    private fun close() {}

    private fun setNotifications(notifications: List<NotificationItem>) {
        val textView = view?.findViewById<TextView>(R.id.textViewNotificationQnt)
        if (textView != null) {
            val notReadQnt = notifications.filter { !it.read }.size
            textView.text = notReadQnt.toString()
            textView.isVisible = notReadQnt > 0
        }
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

    private fun FragmentStreakDetailBinding.setStreak(streak: StreakItem?) {
        if (streak != null) {
            textViewStreakTitle.text = streak.name
            textViewStreakDescription.text = streak.description
            if (streak.backgroundPicture != null) ImageProvider.loadImageFromUrl(detailsPicture, streak.backgroundPicture)
            else if (streak.localBackgroundPicture != null) detailsPicture.setImageResource(ImageProvider.loadLocalImage(streak.localBackgroundPicture, requireContext()))

            cardTag.isVisible = true
            imageViewCardTag.setImageDrawable(
                ContextCompat.getDrawable(
                    this.root.context,
                    when (streak.status) {
                        StreakStatus.PENDING -> R.drawable.ic_bell
                        StreakStatus.STREAK_OVER -> R.drawable.ic_pencil
                        StreakStatus.DAY_DONE -> R.drawable.ic_check
                    }
                )
            )
            textViewCardTag.text = this.root.context.getString(when(streak.status) {
                StreakStatus.PENDING -> R.string.pending
                StreakStatus.STREAK_OVER -> R.string.over
                StreakStatus.DAY_DONE -> R.string.done
            })
            cardTag.background = ContextCompat.getDrawable(
                this.root.context,
                when (streak.status) {
                    StreakStatus.PENDING -> R.drawable.background_rounded_blue
                    StreakStatus.STREAK_OVER -> R.drawable.background_rounded_red
                    StreakStatus.DAY_DONE -> R.drawable.background_rounded_green
                }
            )
        }
    }

    private fun setUser(user: User?) {
        if (user != null) {
            val imageViewProfilePicture = view?.findViewById<ShapeableImageView>(R.id.imageViewProfilePicture)
            if (imageViewProfilePicture != null && user.photoUrl != null) {
                ImageProvider.loadImageFromUrl(imageViewProfilePicture, user.photoUrl.toString())
            }
        }
    }

    private fun FragmentStreakDetailBinding.setState(state: StreakDetailState) {
        when (state) {
            StreakDetailState.DELETING -> {
                imageButtonDelete.isEnabled = false
                buttonStartTimer.isEnabled = false
                buttonCompleteDay.isEnabled = false
            }
            StreakDetailState.DELETED -> {
                // navigate back
                Toast.makeText(requireContext(), "Streak deleted!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            StreakDetailState.IDLE -> {
                imageButtonDelete.isEnabled = true
                buttonStartTimer.isEnabled = true
                buttonCompleteDay.isEnabled = true
            }
            else -> {}
        }

        swipeRefreshLayout.isRefreshing = state == StreakDetailState.LOADING
        progressBar.isVisible = state != StreakDetailState.IDLE
    }

    private fun onCompleteDaySave() {}

    private fun onOptionClicked(option: Int, dialog: Dialog) {
        when (option) {
            CANCEL -> dialog.hide()
//            HIDE -> dialog.hide()
            DELETE -> viewModel.onDeleteStreak(dialog)
        }
    }

    private fun showConfirmationAdvancedModal() {
        Modals.showConfirmationAdvancedDialog(
            requireContext(),
            ::onOptionClicked,
            getString(R.string.are_you_sure_want_delete),
            getString(R.string.cancel),
            getString(R.string.only_hide),
            getString(R.string.delete),
            getString(R.string.confirm_delete_or_hide)
        )
    }
}