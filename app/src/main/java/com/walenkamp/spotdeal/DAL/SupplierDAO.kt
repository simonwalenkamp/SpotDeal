package com.walenkamp.spotdeal.DAL

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.walenkamp.spotdeal.Authentication.AuthManager
import com.walenkamp.spotdeal.Entities.Supplier
import com.walenkamp.spotdeal.Interface.ICallBackSupplier
import com.walenkamp.spotdeal.Interface.ISupplierDAO

class SupplierDAO : ISupplierDAO {
    // Logcat tag
    private val TAG: String = "SupplierDAO"

    // Instance of Firestore
    private val db = FirebaseFirestore.getInstance()

    // AuthManager instance
    private val authManager: AuthManager = AuthManager()

    // Gets the supplier matching the current user
    override fun getSupplier(callback: ICallBackSupplier) {
        db.collection("suppliers").document(authManager.user!!.uid).get().addOnCompleteListener{ task ->
            if(task.isSuccessful) {
                try {
                    val doc = task.result!!
                    val supplier: Supplier? = doc.toObject(Supplier::class.java)
                    supplier?.id = doc.id

                    callback.onFinishSupplier(supplier)
                } catch (e: Exception) {
                    Log.d(TAG, e.message)
                }
            }
        }
    }
}