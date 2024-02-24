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
import androidx.navigation.fragment.findNavController
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentStreakDetailBinding
import com.pedfu.daystreak.presentation.timer.TimerFragmentArgs

class StreakDetailFragment : Fragment() {

    private var _binding: FragmentStreakDetailBinding? = null
    private val binding: FragmentStreakDetailBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun FragmentStreakDetailBinding.setupButtons() {
        buttonStartTimer.setOnClickListener {
            // precisa aparecer modal -> pessoa digita quantidade -> inicia timer
            val args = TimerFragmentArgs.Builder(15).build()
            findNavController().navigate(R.id.action_from_details_to_timer, args.toBundle())
        }
    }

    // mudar nome
    private fun showModal() {
        val dialog = Dialog(this.requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
//        dialog.setContentView(R.id.layout_custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // config text
        // config setonclicklistener

        dialog.show()
    }
}