package com.pedfu.daystreak.presentation.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentTimerBinding
import com.pedfu.daystreak.domain.streak.StreakStatus
import java.util.concurrent.TimeUnit

class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding: FragmentTimerBinding get() = _binding!!

    private lateinit var countDownTimer: CountDownTimer
    private var timeLeft: Long = 0
    private var isRunning = false

    private val args by navArgs<TimerFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            setupTimer()
            setupButtons()
            updateTimerText()
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

    private fun FragmentTimerBinding.setupTimer() {
        val totalTime = TimeUnit.MINUTES.toMillis(args.minutes)
        timeLeft = totalTime
        updateCountDownTimer(timeLeft)
    }

    private fun FragmentTimerBinding.updateTimerText() {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60
        timerText.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun FragmentTimerBinding.setupButtons() {
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        imageButtonStop.setOnClickListener {
            pauseTimer()
            // abrir modal para confirmar
        }

        imageButtonRestart.setOnClickListener {
            resetTimer()
        }

        imageButtonPause.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }
    }

    private fun FragmentTimerBinding.startTimer() {
        isRunning = true

        imageButtonPause.setImageDrawable(
            ContextCompat.getDrawable(
                this.root.context,
                R.drawable.ic_pause_black
            )
        )

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
    }

    private fun FragmentTimerBinding.resetTimer() {
        countDownTimer.cancel()
        timeLeft = TimeUnit.MINUTES.toMillis(args.minutes)
        isRunning = false
        updateTimerText()

        imageButtonPause.setImageDrawable(
            ContextCompat.getDrawable(
                this.root.context,
                R.drawable.ic_unpause_black
            )
        )
    }

    private fun FragmentTimerBinding.updateCountDownTimer(milliSec: Long) {
        countDownTimer = object : CountDownTimer(milliSec, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                isRunning = false
                updateTimerText()
            }
        }
    }
}