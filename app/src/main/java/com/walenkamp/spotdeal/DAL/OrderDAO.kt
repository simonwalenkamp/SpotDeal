package com.walenkamp.spotdeal.DAL

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackOrder
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
                            o.id = doc.id
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
                            o.id = doc.id
                            orderList.add(o)
                        } catch (e: Exception) {
                            Log.d(TAG, e.message)
                        }
                    }
                callback.onFinishOrders(orderList)
            }
    }

    // Gets all valid orders a customer has with a supplier for deal
    override fun getValidOrdersByDeal(callback: ICallbackOrders, customerId: String, dealId: String) {
        val orderList = mutableListOf<Order>()
        db.collection("orders").whereEqualTo("customerId", customerId).whereEqualTo("dealId", dealId).whereEqualTo("valid", true)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful)
                    for (doc in task.result!!) {
                        try {
                            val o = doc.toObject(Order::class.java)
                            o.id = doc.id
                            orderList.add(o)
                        } catch (e: Exception) {
                            Log.d(TAG, e.message)
                        }
                    }
                callback.onFinishOrders(orderList)
            }
    }

    // Gets all invalid orders a customer has with a supplier for deal
    override fun getInvalidOrdersByDeal(callback: ICallbackOrders, customerId: String, dealId: String) {
        val orderList = mutableListOf<Order>()
        db.collection("orders").whereEqualTo("customerId", customerId).whereEqualTo("dealId", dealId).whereEqualTo("valid", false)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful)
                    for (doc in task.result!!) {
                        try {
                            val o = doc.toObject(Order::class.java)
                            o.id = doc.id
                            orderList.add(o)
                        } catch (e: Exception) {
                            Log.d(TAG, e.message)
                        }
                    }
                callback.onFinishOrders(orderList)
            }
    }

    // Updates orders
    override fun updateOrders(callback: ICallbackOrders, orders: List<Order>) {
        var updates = 0
        for(o in orders) {
            val ref = db.collection("orders").document(o.id)
                ref.set(o).addOnCompleteListener{ task ->
                if(task.isSuccessful) {
                    updates += 1
                    if(updates == orders.size) {
                        callback.onFinishOrders(orders)
                    }
                }
            }
        }
    }

    // Gets order by its id
    override fun getOrderById(id: String, callback: ICallbackOrder) {
        db.collection("orders").document(id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                try {
                    val doc = task.result!!
                    val o: Order? = doc.toObject(Order::class.java)
                    o?.id = doc.id

                    callback.onFinishOrder(o)
                } catch (e: Exception) {
                    Log.d(TAG, e.message)
                }
            }
        }
    }
}