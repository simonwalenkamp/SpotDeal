package com.walenkamp.spotdeal.DAL

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackOrders
import com.walenkamp.spotdeal.Interface.IOrderDAO

class OrderDAO : IOrderDAO {
    // Logcat tag
    private val TAG: String = "OrderDAO"

    // Instance of Firestore
    private val db = FirebaseFirestore.getInstance()

    // Gets all orders for a supplier
    override fun getOrders(callback: ICallbackOrders, supplierId: String) {
        val orderList = mutableListOf<Order>()
        db.collection("orders").whereEqualTo("supplierId", supplierId)
            .get().addOnCompleteListener { task ->
                if(task.isSuccessful)
                    for (doc in task.result!!) {
                        try {
                            val o = doc.toObject(Order::class.java)
                            orderList.add(o)
                        } catch (e: Exception) {
                            Log.d(TAG, e.message)
                        }
                    }
                callback.onFinishOrders(orderList)
            }
    }

    // Gets all orders a customer has with a supplier
    override fun getOrdersByCustomer(callback: ICallbackOrders, customerId: String, supplierId: String) {
        val orderList = mutableListOf<Order>()
        db.collection("orders").whereEqualTo("customerId", customerId).whereEqualTo("supplierId", supplierId)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful)
                    for (doc in task.result!!) {
                        try {
                            val o = doc.toObject(Order::class.java)
                            orderList.add(o)
                        } catch (e: Exception) {
                            Log.d(TAG, e.message)
                        }
                    }
                callback.onFinishOrders(orderList)
            }
    }
}