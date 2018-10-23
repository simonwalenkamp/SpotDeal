package com.walenkamp.spotdeal.Authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.walenkamp.spotdeal.Interface.ICallBackUser

class AuthManager {

    // FireAuth instance
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // User instance
    var user: FirebaseUser? = mAuth.currentUser

    // Authenticates the user with the given information
    fun login(email: String, password: String, callBack: ICallBackUser) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user = mAuth.currentUser
                    callBack.onFinishFirebaseUser(user)
                } else {
                    callBack.onFinishFirebaseUser(user)
                }
            }
    }
}