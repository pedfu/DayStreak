package com.pedfu.daystreak.presentation.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentHomeBinding
import com.pedfu.daystreak.domain.user.User
import com.pedfu.daystreak.presentation.MainActivity
import com.pedfu.daystreak.presentation.header.notification.NotificationDialogFragment

class HeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var viewModel: HeaderViewModel
    fun setViewModel(viewModel: HeaderViewModel) {
        this.viewModel = viewModel
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_notification_header, this, true)
        findViewById<ConstraintLayout>(R.id.frameLayoutBell).setOnClickListener {
            showNotificationsModal()
        }
    }

    private fun showNotificationsModal() {
        val notificationHeader = NotificationDialogFragment()
    }

//    private fun FragmentHomeBinding.setUser(user: User?) {
//        if (user != null) {
////            Glide.with(requireContext())
////                .load(user.photoUrl)
////                .into(imageViewProfilePicture)
//
//            startMainText.text = getString(R.string.hi_username, user.username)
//            dayStreakQnt.text = user.maxStreak.toString()
//        }
//    }

//    imageViewProfilePicture.setOnClickListener {
//        (activity as? MainActivity)?.signOutAndStartSignInActivity()
//    }

//    frameLayoutBell.setOnClickListener {
//        (activity as? MainActivity)?.showNotificationsDialog(requireContext())
//    }
}