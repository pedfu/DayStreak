package com.pedfu.daystreak.presentation.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding: FragmentSignupBinding get() = _binding!!
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            observeViewModel()
            setupButtons()
            setupTextEdit()
        }
    }

    private fun FragmentSignupBinding.observeViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { setState(it) }
    }

    private fun FragmentSignupBinding.setupButtons() {
        signUpButton.setOnClickListener {
            viewModel.onSignupClicked()
        }
        signInButton.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun FragmentSignupBinding.setupTextEdit() {
        editTextEmail.addTextChangedListener { viewModel.onEmailChanged(it.toString()) }
        editTextFirstName.addTextChangedListener { viewModel.onFirstNameChanged(it.toString()) }
        editTextLastName.addTextChangedListener { viewModel.onLastNameChanged(it.toString()) }
        editTextUsername.addTextChangedListener { viewModel.onUsernameChanged(it.toString()) }
        editTextPassword.addTextChangedListener { viewModel.onPasswordChanged(it.toString()) }
    }

    private fun FragmentSignupBinding.setState(state: SignupState) {
        signUpButton.isClickable = state == SignupState.READY
        signUpButton.error = if (state == SignupState.ERROR) "Wrong user or password" else null

        if (state == SignupState.DATA_SENT) {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_from_signup_to_home)
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_from_signup_to_login)
    }
}