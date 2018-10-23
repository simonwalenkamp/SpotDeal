package com.walenkamp.spotdeal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.walenkamp.spotdeal.Authentication.AuthManager

class MainActivity : AppCompatActivity() {

    private var isLoggedIn : Boolean = false

    // AuthManager instance
    private val authManager: AuthManager = AuthManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkUser()
        if(isLoggedIn) {
            startSearch()
        } else {
            startLogin()
        }
    }

    // Starts the LoginFragment
    private fun startLogin() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_activity, LoginFragment.newInstance(), "loginFragment")
            .commit()
    }

    // Start the SearchActivity
    private fun startSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Checks if a user is logged in
    private fun checkUser() {
        if(authManager.user != null) {
            isLoggedIn = true
        }
    }
}
