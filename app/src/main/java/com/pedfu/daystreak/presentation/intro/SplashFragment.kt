package com.pedfu.daystreak.presentation.intro

import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentHomeBinding
import com.pedfu.daystreak.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding: FragmentSplashBinding get() = _binding!!

    private val viewModel: SplashViewModel by viewModels()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = DisplayMetrics()
        this.requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val marginStart = -(screenWidth / 2)
        val layoutParams = binding.sunIcon.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginStart = marginStart
        binding.sunIcon.layoutParams = layoutParams

        val sunRiseAnimation = AnimationUtils.loadAnimation(this.context, R.anim.sun_rise)
        binding.sunIcon.startAnimation(sunRiseAnimation)

        binding.run {
            observeViewModel()
        }
    }

    private fun FragmentSplashBinding.observeViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { setState(it) }
    }

    private fun FragmentSplashBinding.setState(state: SplashState) {
        when (state) {
            SplashState.LOGGED_IN -> {
                sunIcon.isVisible = false
                textView.isVisible = false
                textViewDescription.isVisible = false

                findNavController().popBackStack(R.id.splashFragment, false)
                findNavController().navigate(R.id.action_from_splash_to_home)
            }
            SplashState.NOT_LOGGED_IN -> {
                sunIcon.isVisible = false
                textView.isVisible = false
                textViewDescription.isVisible = false

                findNavController().popBackStack(R.id.splashFragment, false)
                findNavController().navigate(R.id.action_from_splash_to_intro)
            }
            else -> null
        }
    }
}