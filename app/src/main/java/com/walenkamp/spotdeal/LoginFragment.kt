package com.walenkamp.spotdeal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.walenkamp.spotdeal.Authentication.AuthManager
import com.walenkamp.spotdeal.Interface.ICallBackUser
import kotlinx.android.synthetic.main.login_fragment.*




class LoginFragment : androidx.fragment.app.Fragment() {

    // AuthManager instance
    private val authManager: AuthManager = AuthManager()

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onStart() {
        super.onStart()
        login_btn.setOnClickListener{
            login()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    // Uses the AuthManager to authenticate a user
    // If for some reason the user is not authenticated a snackbar while show with a message why
    private fun login() {
        progress.visibility = View.VISIBLE
        if(email_et.text.toString() == "" || email_et.text == null) {
            progress.visibility = View.INVISIBLE
            Snackbar.make(view!!, R.string.error_email, Snackbar.LENGTH_LONG).show()
            return
        } else if (password_et.text.toString() == "" || password_et.text == null) {
            progress.visibility = View.INVISIBLE
            Snackbar.make(view!!, R.string.error_password, Snackbar.LENGTH_LONG).show()
            return
        }
            authManager.login(email_et.text.toString(), password_et.text.toString(), object: ICallBackUser {
                override fun onFinishFirebaseUser(user: FirebaseUser?) {
                    if(authManager.user != null) {
                        startSearch()
                    } else {
                        progress.visibility = View.INVISIBLE
                        Snackbar.make(view!!, R.string.error_wrong_combination, Snackbar.LENGTH_LONG).show()
                    }
                }
            })
    }

    // Starts the SearchActivity
    private fun startSearch() {
        val intent = Intent(this.context, SearchActivity::class.java)
        startActivity(intent)
    }
}