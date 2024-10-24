package com.pedfu.daystreak.presentation.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentTimerBinding
import com.pedfu.daystreak.domain.streak.StreakStatus
import com.pedfu.daystreak.utils.Modals
import com.pedfu.daystreak.utils.lazyViewModel
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding: FragmentTimerBinding get() = _binding!!

    private val viewModel by lazyViewModel {
        TimerViewModel(args.minutes)
    }

    private lateinit var countDownTimer: CountDownTimer
    private var timeLeft: Long = 0
    private var isRunning = false

    private val args by navArgs<TimerFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            setupButtons()
            updateTimerText()
            observeViewModel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
        _binding = null
    }

    private fun FragmentTimerBinding.observeViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { setState(it) }
        viewModel.totalTimerLiveData.observe(viewLifecycleOwner) { setTimer(it) }
    }

    private fun FragmentTimerBinding.setState(state: TimerState) {
        timerText.isClickable = state == TimerState.IDLE
        timerText.isActivated = state == TimerState.IDLE
        timerText.isEnabled = state == TimerState.IDLE
    }

    private fun FragmentTimerBinding.setTimer(timeInSeconds: Long) {
        val totalTime = TimeUnit.SECONDS.toMillis(timeInSeconds)
        timeLeft = totalTime * 1000
        updateCountDownTimer(timeLeft)
        updateTimerText()
    }

    private fun FragmentTimerBinding.updateTimerText() {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60

        // Convert minutes and seconds to strings
        val minutesStr = if (minutes < 10) "0$minutes" else minutes.toString().substring(0, 2)
        val secondsStr = if (seconds < 10) "0$seconds" else seconds.toString().substring(0, 2)

        // Combine them into the desired format
        val formatted = "$minutesStr:$secondsStr"

        // Set the formatted string to the timer text
        timerText.setText(formatted)
    }

    private fun FragmentTimerBinding.setupButtons() {
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        imageButtonStop.setOnClickListener {
            pauseTimer()
            viewModel.stopTimer()
            openConfirmationModal()
        }
        imageButtonRestart.setOnClickListener { resetTimer() }
        imageButtonPause.setOnClickListener {
            when (isRunning) {
                true -> pauseTimer()
                else -> startTimer()
            }
        }
    }

    private fun openConfirmationModal() {
        Modals.showConfirmationDialog(
            requireContext(),
            ::onConfirmCreateTrack,
            getString(R.string.would_you_like_to_save_track),
            getString(R.string.track_saving_added_to_daily_progress)
        )
    }
    private fun onConfirmCreateTrack() {
        viewModel.onConfirmCreateTrack()
    }

    private fun FragmentTimerBinding.startTimer() {
        viewModel.startTimer()
        imageButtonPause.setImageDrawable(
            ContextCompat.getDrawable(
                this.root.context,
                R.drawable.ic_pause_black
            )
        )
        isRunning = true
        updateCountDownTimer(timeLeft)
        countDownTimer.start()

        timerText.isClickable = false
        timerText.isActivated = false
        timerText.isEnabled = false
    }

    private fun FragmentTimerBinding.pauseTimer() {
        countDownTimer.cancel()
        isRunning = false
        imageButtonPause.setImageDrawable(
            ContextCompat.getDrawable(
                this.root.context,
                R.drawable.ic_unpause_black
            )
        )

        timerText.isClickable = true
        timerText.isActivated = true
        timerText.isEnabled = true
    }

//    private fun FragmentTimerBinding.resetTimer() {
//        countDownTimer.cancel()
////        timeLeft = TimeUnit.MILLISECONDS.toSeconds(viewModel.timeLeft)
//        viewModel.updateTimeLeft(900)
//        viewModel.startTimer()
//        isRunning = false
//        updateTimerText()
//        imageButtonPause.setImageDrawable(
//            ContextCompat.getDrawable(
//                this.root.context,
//                R.drawable.ic_unpause_black
//            )
//        )
//
//        timerText.isClickable = true
//        timerText.isActivated = true
//        timerText.isEnabled = true
//    }

    private fun FragmentTimerBinding.resetTimer() {
        pauseTimer()

        // restart timer
        setTimer(viewModel.totalTimer)

        // Habilita a interação com o campo de texto do timer
        timerText.isClickable = true
        timerText.isActivated = true
        timerText.isEnabled = true
    }

    private fun FragmentTimerBinding.updateCountDownTimer(milliSec: Long) {
        countDownTimer = object : CountDownTimer(milliSec, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                viewModel.updateTimeLeft(millisUntilFinished / 1000)
                updateTimerText()
            }
            override fun onFinish() {
                isRunning = false
                updateTimerText()
            }
        }
    }
}