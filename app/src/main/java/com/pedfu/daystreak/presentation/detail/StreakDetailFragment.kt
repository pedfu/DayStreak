package com.pedfu.daystreak.presentation.detail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentStreakDetailBinding
import com.pedfu.daystreak.domain.streak.StreakItem
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

    private fun FragmentStreakDetailBinding.setStreak(streak: StreakItem?) {
        if (streak != null) {
            textViewStreakTitle.text = streak.name
            textViewStreakDescription.text = streak.description
            ImageProvider.loadImageFromUrl(detailsPicture, streak.backgroundPicture)
        }
    }

    private fun FragmentStreakDetailBinding.setState(state: StreakDetailState) {
        swipeRefreshLayout.isRefreshing = state == StreakDetailState.LOADING
        progressBar.isVisible = state != StreakDetailState.IDLE
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