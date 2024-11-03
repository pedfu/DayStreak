package com.pedfu.daystreak.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pedfu.daystreak.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private fun setupBackButton() {
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigateUp()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()
        binding.run {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FragmentSettingsBinding.setupButtons() {
        linearLayoutBadges.setOnClickListener {  }
        linearLayoutDayStreaks.setOnClickListener {  }
        linearLayoutHiddenDayStreaks.setOnClickListener {  }
        linearLayoutSettings.setOnClickListener {  }
    }
}