package com.pedfu.daystreak.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.R
import com.pedfu.daystreak.presentation.signin.SignInActivity

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val authorizationManager = Inject.authorizationManager

    private val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

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