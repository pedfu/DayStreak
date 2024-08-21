package com.pedfu.daystreak.presentation.header.notification

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentNotificationDialogBinding
import com.pedfu.daystreak.databinding.FragmentSelectCreateTypeDialogBinding
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.presentation.creation.category.CategoryCreationDialogFragment
import com.pedfu.daystreak.presentation.creation.streak.StreakCreationDialogFragment
import com.pedfu.daystreak.presentation.home.adapters.NotificationAdapter
import com.pedfu.daystreak.utils.lazyViewModel

class NotificationDialogFragment : DialogFragment() {
    private var _binding: FragmentNotificationDialogBinding? = null
    private val binding: FragmentNotificationDialogBinding get() = _binding!!

    private val viewModel by lazyViewModel {
        NotificationViewModel()
    }

    private val notificationAdapter = NotificationAdapter(::onItemClick, ::onAcceptChallenge, ::onDeclineChallenge)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        binding.run {
            buttonClearAll.setOnClickListener {
                viewModel.cleanAll()
            }
            buttonMarkAllRead.setOnClickListener {
                viewModel.markAllAsRead()
            }
            buttonClose.setOnClickListener {
                dismiss()
            }

            observeViewModel()
        }
    }

    private fun FragmentNotificationDialogBinding.observeViewModel() {
        viewModel.notificationsLiveData.observe(viewLifecycleOwner) { setNotifications(it) }
        viewModel.stateLiveData.observe(viewLifecycleOwner) { setState(it) }
    }

    private fun FragmentNotificationDialogBinding.setState(state: NotificationState) {
        when (state) {
            NotificationState.MARKING_READ -> {
                buttonMarkAllRead.isLoading = true
                buttonMarkAllRead.isEnabled = false
            }
            NotificationState.CLEARING -> {
                buttonClearAll.isLoading = true
                buttonClearAll.isEnabled = false
            }
            else -> {
                buttonMarkAllRead.isLoading = false
                buttonMarkAllRead.isEnabled = true
                buttonClearAll.isLoading = false
                buttonClearAll.isEnabled = true
            }
        }
    }

    private fun FragmentNotificationDialogBinding.setNotifications(notifications: List<NotificationItem>) {
        recyclerViewNotifications.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerViewNotifications.adapter = notificationAdapter.apply {
            items = notifications
        }
        ViewCompat.setNestedScrollingEnabled(recyclerViewNotifications, true)
    }

    private fun onItemClick(id: Long) {
        viewModel.markItemRead(id)
    }
    private fun onAcceptChallenge(id: Long) {
        viewModel.onAcceptChallenge(id)
    }
    private fun onDeclineChallenge(id: Long) {
        viewModel.onDeclineChallenge(id)
    }
}