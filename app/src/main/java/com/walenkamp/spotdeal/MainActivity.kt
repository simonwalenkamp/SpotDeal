package com.walenkamp.spotdeal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private var isLoggedIn : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(isLoggedIn) {
            // TODO: implement what happens if user is already logged in
        } else {
            startLogin()
        }
    }

    private fun startLogin() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_activity, LoginFragment.newInstance(), "loginFragment")
            .commit()
    }
}
