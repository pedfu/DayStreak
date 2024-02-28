package com.pedfu.daystreak.presentation.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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
import com.pedfu.daystreak.domain.user.User
import kotlinx.coroutines.runBlocking

class SignInActivity : AppCompatActivity() {
    companion object {
        private const val SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        // redirect to main activity if logged
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // trocar o runbloacking
            runBlocking {
                Inject.userRepository.saveUser(
                    User(
                        userId = 0,
                        username = currentUser.displayName,
                        email = currentUser.email,
                        uid = currentUser.uid,
                        tenantId = currentUser.tenantId,
                        photoUrl = currentUser.photoUrl
                    )
                )
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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
                    runBlocking {
                        val currentUser = auth.currentUser
                        if (currentUser != null) {
                            Inject.userRepository.saveUser(
                                User(
                                    userId = 0,
                                    username = currentUser.displayName,
                                    email = currentUser.email,
                                    uid = currentUser.uid,
                                    tenantId = currentUser.tenantId,
                                    photoUrl = currentUser.photoUrl
                                )
                            )
                        }
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Auth failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}