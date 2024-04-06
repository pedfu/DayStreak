package com.pedfu.daystreak.presentation.detail

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentStreakDetailBinding
import com.pedfu.daystreak.presentation.timer.TimerFragmentArgs
import com.pedfu.daystreak.utils.Modals

val CANCEL = 1
val HIDE = 2
val DELETE = 3

class StreakDetailFragment : Fragment() {

    private var _binding: FragmentStreakDetailBinding? = null
    private val binding: FragmentStreakDetailBinding get() = _binding!!


    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()
        binding.run {
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

    private fun FragmentStreakDetailBinding.setupButtons() {
        buttonStartTimer.setOnClickListener {
            // precisa aparecer modal -> pessoa digita quantidade -> inicia timer
            val args = TimerFragmentArgs.Builder(15).build()
            findNavController().navigate(R.id.action_from_details_to_timer, args.toBundle())
        }
        imageButtonDelete.setOnClickListener {
            showConfirmationAdvancedModal()
        }
        buttonCompleteDay.setOnClickListener {
            Modals.showCompleteDayDialog(requireContext(), ::onCompleteDaySave)
        }
    }

    private fun onCompleteDaySave() {}

    private fun onOptionClicked(option: Int, dialog: Dialog) {
        when (option) {
            CANCEL -> dialog.hide()
            HIDE -> dialog.hide()
            CANCEL -> dialog.hide()
        }
    }

    private fun showConfirmationAdvancedModal() {
        Modals.showConfirmationAdvancedDialog(
            requireContext(),
            ::onOptionClicked,
            getString(R.string.are_you_sure_want_delete),
            getString(R.string.cancel),
            getString(R.string.hide),
            getString(R.string.delete),
            getString(R.string.confirm_delete_or_hide)
        )
    }
}