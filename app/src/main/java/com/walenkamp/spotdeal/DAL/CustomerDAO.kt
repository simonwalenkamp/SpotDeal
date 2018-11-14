package com.walenkamp.spotdeal.DAL

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackCustomer
import com.walenkamp.spotdeal.Interface.ICallbackCustomers
import com.walenkamp.spotdeal.Interface.ICustomerDAO

class CustomerDAO: ICustomerDAO {
    // Logcat tag
    private val TAG: String = "CustomerDAO"

    // Instance of Firestore
    private val db = FirebaseFirestore.getInstance()

    // Gets all customers that has orders with the supplier
    override fun getCustomers(callback: ICallbackCustomers, orderList: List<Order>) {
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

    // Gets specific customer
    override fun getCustomerById(id: String, callback: ICallbackCustomer) {
        db.collection("customers").document(id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                try {
                    val doc = task.result!!
                    val c: Customer? = doc.toObject(Customer::class.java)
                    c?.id = doc.id

                    callback.onFinishCustomer(c)
                } catch (e: Exception) {
                    Log.d(TAG, e.message)
                }
            }
        }
    }
}