package com.pedfu.daystreak

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pedfu.daystreak.presentation.signin.SignInActivity

class MainActivity : AppCompatActivity() {
    private val authorizationManager = Inject.authorizationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (authorizationManager.token == null) {
            startSignInActivity()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    // used in settings
    fun signOutAndStartSignInActivity() {
        authorizationManager.notifyUnauthorized()
        startSignInActivity()
    }

    private fun startSignInActivity() {
        val intent = Intent(this@MainActivity, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}