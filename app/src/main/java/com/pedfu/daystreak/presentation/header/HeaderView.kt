package com.pedfu.daystreak.presentation.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.imageview.ShapeableImageView
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
    private var navigateToAction: (() -> Unit)? = null
    private var showNotificationsModal: (() -> Unit)? = null
    fun setViewModel(viewModel: HeaderViewModel) {
        this.viewModel = viewModel
    }
    fun setNavigateToAction(action: () -> Unit) {
        this.navigateToAction = action
    }
    fun setShowNotificationsModal(action: () -> Unit) {
        this.showNotificationsModal = action
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_notification_header, this, true)
        findViewById<ConstraintLayout>(R.id.frameLayoutBell).setOnClickListener {
            showNotificationsModal?.invoke()
        }
        findViewById<ShapeableImageView>(R.id.imageViewProfilePicture).setOnClickListener {
            navigateToAction?.invoke()
        }
    }
}