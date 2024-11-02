package com.pedfu.daystreak.presentation.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentTimerBinding
import com.pedfu.daystreak.utils.Modals
import com.pedfu.daystreak.utils.lazyViewModel
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
        timeLeft = TimeUnit.SECONDS.toMillis(timeInSeconds)
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
        timerTextInput.setText("${String.format("%02d", args.minutes)}:00")
        timerTextInput.addTextChangedListener(object : TextWatcher {
            private var currentText = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != currentText) {
                    val cleanInput = s.toString().replace(":", "")
                    if (cleanInput.length == 4) {
                        viewModel.updateTotalTimer(cleanInput)
                    }

                    // Format the string as NN:NN
                    if (cleanInput.length <= 4) {
                        val formatted = StringBuilder(cleanInput)
                        if (cleanInput.length >= 3) {
                            formatted.insert(2, ":")
                        }
                        currentText = formatted.toString()
                        timerTextInput.setText(currentText)
                        timerTextInput.setSelection(currentText.length)
                    } else {
                        timerTextInput.setText(currentText)
                        timerTextInput.setSelection(currentText.length)
                    }
                }
            }
        })
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

    private fun FragmentTimerBinding.setTimerText(started: Boolean=true) {
        timerTextInput.isVisible = !started
        timerText.isVisible = started

        timerTextInput.isClickable = !started
        timerTextInput.isActivated = !started
        timerTextInput.isEnabled = !started
    }

    private fun FragmentTimerBinding.startTimer() {
        setTimerText(true)

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

    private fun FragmentTimerBinding.resetTimer() {
        setTimerText(false)
        pauseTimer()

        // restart timer
        setTimer(viewModel.totalTimer)
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