package com.walenkamp.spotdeal.DAL

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.walenkamp.spotdeal.Authentication.AuthManager
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Supplier
import com.walenkamp.spotdeal.Interface.ICallBackSupplier
import com.walenkamp.spotdeal.Interface.ICallbackCustomers
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackOrders
import kotlin.coroutines.experimental.suspendCoroutine


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

    // Gets all customers that has orders with the supplier
    fun getCustomers(callback: ICallbackCustomers, orderList: List<Order>) {
        val customerList = mutableListOf<Customer>()

            db.collection("customers").get().addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    for (doc in task.result!!) {
                        val customer = doc.toObject(Customer::class.java)
                        customer.id = doc.id
                        for (o in orderList) {
                            if (customer.id == o.customerId && !customerList.contains(customer)) {
                                customerList.add(customer)
                            }
                        }
                    }
                }
                callback.onFinishCustomers(customerList)
            }
    }

    // Gets all orders for a supplier
    fun getOrders(callback: ICallbackOrders, supplierId: String) {
        val orderList = mutableListOf<Order>()
        db.collection("orders").whereEqualTo("supplierId", supplierId)
            .get().addOnCompleteListener { task ->
                if(task.isSuccessful)
                    for (doc in task.result!!) {
                        val o = doc.toObject(Order::class.java)
                        orderList.add(o)
                    }
                callback.onFinishOrders(orderList)
            }
    }
}