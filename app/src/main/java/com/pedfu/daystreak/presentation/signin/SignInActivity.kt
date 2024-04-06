package com.pedfu.daystreak.presentation.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pedfu.daystreak.presentation.MainActivity
import com.pedfu.daystreak.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.stateLiveData.observe(this) { setState(it) }
    }

    private fun setState(state: SignInState) {
        when (state) {
            SignInState.IDLE -> return
            SignInState.LOADING -> return
            SignInState.LOGGED_IN -> navigateToMainActivity()
            SignInState.ERROR -> Toast.makeText(this, "Google sign in failed:", Toast.LENGTH_SHORT).show()
        }
    }

    fun signIn() {
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}