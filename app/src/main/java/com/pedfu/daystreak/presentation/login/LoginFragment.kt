package com.pedfu.daystreak.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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

    private fun FragmentLoginBinding.observeViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { setState(it) }
    }

    private fun FragmentLoginBinding.setupButtons() {
        signInButton.setOnClickListener {
            viewModel.onLoginClicked()
        }
        signUpButton.setOnClickListener {
            navigateToSignup()
        }
    }

    private fun FragmentLoginBinding.setupTextEdit() {
        editTextPassword.addTextChangedListener { viewModel.onPasswordChanged(it.toString()) }
        editTextUsernameOrEmail.addTextChangedListener { viewModel.onUsernameOrEmailChanged(it.toString()) }
    }

    private fun FragmentLoginBinding.setState(state: LoginState) {
        signInButton.isClickable = state == LoginState.READY
        signInButton.isEnabled = state == LoginState.READY
//        signInButton.error = if (state == LoginState.ERROR) "Wrong user or password" else null

        if (state == LoginState.LOGGED_IN) {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_from_login_to_home)
    }

    private fun navigateToSignup() {
        findNavController().navigate(R.id.action_from_login_to_signup)
    }
}