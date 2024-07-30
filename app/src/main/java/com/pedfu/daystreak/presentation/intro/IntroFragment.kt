package com.pedfu.daystreak.presentation.intro

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentIntroBinding
import com.pedfu.daystreak.presentation.signin.SignInActivity

class IntroFragment : Fragment() {

    private var _binding: FragmentIntroBinding? = null
    private val binding: FragmentIntroBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainPhrase = getString(R.string.intro_main_phrase)
        val words = mainPhrase.split(" ")
        val lastTwoWords = words.subList(words.size - 2, words.size)

        val spannableStr = SpannableString(mainPhrase)
        val startIndex = mainPhrase.indexOf(lastTwoWords.first())

        val orange = this.context?.let { context ->
            ContextCompat.getColor(context, R.color.orange_text)
        } ?: Color.parseColor("#FFA500")
        spannableStr.setSpan(ForegroundColorSpan(orange), startIndex, mainPhrase.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.mainPhrase.text = spannableStr

        binding.linearLayoutGoogleButton.setOnClickListener {
            (activity as? SignInActivity)?.signIn()
        }
        binding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_from_intro_to_login)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}