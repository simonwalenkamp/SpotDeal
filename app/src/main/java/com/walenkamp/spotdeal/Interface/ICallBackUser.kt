package com.walenkamp.spotdeal.Interface

import com.google.firebase.auth.FirebaseUser



interface ICallBackUser {
    fun onFinishFirebaseUser(user: FirebaseUser?)
}