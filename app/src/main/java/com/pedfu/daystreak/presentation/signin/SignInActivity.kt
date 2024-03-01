package com.pedfu.daystreak.presentation.signin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.MainActivity
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.ActivitySignInBinding
import com.pedfu.daystreak.databinding.FragmentHomeBinding
import com.pedfu.daystreak.domain.user.User
import kotlinx.coroutines.runBlocking

class SignInActivity : AppCompatActivity() {
    companion object {
        private const val SIGN_IN = 9001
    }
    private lateinit var binding: ActivitySignInBinding

    private lateinit var auth: FirebaseAuth

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        binding = ActivitySignInBinding.inflate(layoutInflater)

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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currUser = auth.currentUser
                    currUser?.getIdToken(true)?.addOnCompleteListener { firebaseTask ->
                        if (firebaseTask.isSuccessful) {
                            val firebaseToken = firebaseTask.result.token

                            // save token
                            viewModel.saveUser(currUser, firebaseToken)
                        } else {
                            Toast.makeText(this, "Auth failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Auth failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}