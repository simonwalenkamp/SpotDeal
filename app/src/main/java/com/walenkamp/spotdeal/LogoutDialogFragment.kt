package com.walenkamp.spotdeal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.walenkamp.spotdeal.Authentication.AuthManager
import kotlinx.android.synthetic.main.dialog_logout.*

class LogoutDialogFragment: DialogFragment() {

    // AuthManager instance
    private val authManager: AuthManager = AuthManager()

    companion object {
        fun newInstance(): LogoutDialogFragment {
            return LogoutDialogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_logout, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog_log_out.setOnClickListener{
            logout()
        }
        dialog_cancel.setOnClickListener{
            cancel()
        }
    }

    // Signs the user out and returns to MainActivity
    private fun logout(){
        authManager.logOut()

        val intent = Intent(this.context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    // Closes the dialog
    private fun cancel() {
        dialog.dismiss()
    }
}