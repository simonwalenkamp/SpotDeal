package com.walenkamp.spotdeal.DAL

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.walenkamp.spotdeal.Authentication.AuthManager
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Supplier
import com.walenkamp.spotdeal.Interface.ICallBackSupplier
import com.walenkamp.spotdeal.Interface.ICallbackCustomers
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackDeals
import com.walenkamp.spotdeal.Interface.ICallbackOrders


class DatabaseHelper {

    // Logcat tag
    private val TAG: String = "DatabaseHelper"

    // Instance of Firestore
    private val db = FirebaseFirestore.getInstance()

    // AuthManager instance
    private val authManager: AuthManager = AuthManager()


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

    // Gets all orders for a supplier
    fun getOrders(callback: ICallbackOrders, supplierId: String) {
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

    // Gets all valid deals for customer
    fun getValidDeals(callback: ICallbackDeals, orderList: List<Order>, supplierId: String) {
        val dealList = mutableListOf<Deal>()
        db.collection("deals").whereEqualTo("supplierId", supplierId).get().addOnCompleteListener { task ->
            if(task.isSuccessful) {
                for (doc in task.result!!) {
                    try {
                        val deal = doc.toObject(Deal::class.java)
                        deal.id = doc.id
                        for (o in orderList) {
                            if (deal.id == o.dealId && !dealList.contains(deal) && o.valid) {
                                dealList.add(deal)
                            }
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, e.message)
                    }
                }
            }
            callback.onFinishDeals(dealList)
        }
    }

    // Gets all invalid deals for customer
    fun getInvalidDeals(callback: ICallbackDeals, orderList: List<Order>, supplierId: String) {
        val dealList = mutableListOf<Deal>()
        db.collection("deals").whereEqualTo("supplierId", supplierId).get().addOnCompleteListener { task ->
            if(task.isSuccessful) {
                for (doc in task.result!!) {
                    try {
                        val deal = doc.toObject(Deal::class.java)
                        deal.id = doc.id
                        for (o in orderList) {
                            if (deal.id == o.dealId && !dealList.contains(deal) && !o.valid) {
                                dealList.add(deal)
                            }
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, e.message)
                    }
                }
            }
            callback.onFinishDeals(dealList)
        }
    }

    // Gets all deals for customer
    fun getAllDeals(callback: ICallbackDeals, orderList: List<Order>, supplierId: String) {
        val dealList = mutableListOf<Deal>()
        db.collection("deals").whereEqualTo("supplierId", supplierId).get().addOnCompleteListener { task ->
            if(task.isSuccessful) {
                for (doc in task.result!!) {
                    try {
                        val deal = doc.toObject(Deal::class.java)
                        deal.id = doc.id
                        for (o in orderList) {
                            if (deal.id == o.dealId && !dealList.contains(deal)) {
                                dealList.add(deal)
                            }
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, e.message)
                    }
                }
            }
            callback.onFinishDeals(dealList)
        }
    }
}