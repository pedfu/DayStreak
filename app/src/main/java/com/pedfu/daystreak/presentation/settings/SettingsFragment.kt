package com.pedfu.daystreak.presentation.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentHomeBinding
import com.pedfu.daystreak.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        linearLayoutSignOut.setOnClickListener {  }
        linearLayoutGoogleSignIn.setOnClickListener {  }
    }
}