package com.walenkamp.spotdeal.DAL

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackCustomers

class CustomerDAO {
    // Logcat tag
    private val TAG: String = "CustomerDAO"

    // Instance of Firestore
    private val db = FirebaseFirestore.getInstance()

    // Gets all customers that has orders with the supplier
    fun getCustomers(callback: ICallbackCustomers, orderList: List<Order>) {
        val customerList = mutableListOf<Customer>()
        db.collection("customers").get().addOnCompleteListener { task ->
            if(task.isSuccessful) {
                for (doc in task.result!!) {
                    try {
                        val customer = doc.toObject(Customer::class.java)
                        customer.id = doc.id
                        for (o in orderList) {
                            if (customer.id == o.customerId && !customerList.contains(customer)) {
                                customerList.add(customer)
                            }
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, e.message)
                    }
                }
            }
            callback.onFinishCustomers(customerList)
        }
    }
}