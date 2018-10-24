package com.walenkamp.spotdeal.DAL

import com.google.firebase.firestore.FirebaseFirestore
import com.walenkamp.spotdeal.Authentication.AuthManager
import com.walenkamp.spotdeal.Entities.Supplier
import com.walenkamp.spotdeal.Interface.ICallBackSupplier

class DatabaseHelper {

    // Instance of Firestore
    private val db = FirebaseFirestore.getInstance()

    // AuthManager instance
    private val authManager: AuthManager = AuthManager()

    // Gets the supplier matching the current user
    fun getSupplier(callback: ICallBackSupplier) {
        db.collection("suppliers").document(authManager.user!!.uid).get().addOnCompleteListener{ task ->
            if(task.isSuccessful) {
                val doc = task.result!!
                val supplier: Supplier? = doc.toObject(Supplier::class.java)
                supplier?.id = doc.id

                callback.onFinishSupplier(supplier)
            }
        }
    }
}