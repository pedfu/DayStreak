package com.pedfu.daystreak.presentation.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentStreakDetailBinding
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.domain.streak.StreakStatus
import com.pedfu.daystreak.domain.user.User
import com.pedfu.daystreak.presentation.MainActivity
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

class ProfileFragment : Fragment() {

    private var _binding: FragmentStreakDetailBinding? = null
    private val binding: FragmentStreakDetailBinding get() = _binding!!

    private val viewModel by lazyViewModel {
        ProfileViewModel()
    }

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()
        binding.run {
            observeViewModel()
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

    private fun FragmentStreakDetailBinding.observeViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { setState(it) }
    }

    private fun FragmentStreakDetailBinding.setState(state: ProfileState) {
        when (state) {
            else -> {}
        }
    }
}