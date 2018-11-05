package com.walenkamp.spotdeal.DAL

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackDealImage
import com.walenkamp.spotdeal.Interface.ICallbackDeals
import com.walenkamp.spotdeal.Interface.IDealDAO
import java.util.*

class DealDAO: IDealDAO {
    // Logcat tag
    private val TAG: String = "DealDAO"

    // Instance of Firestore
    private val db = FirebaseFirestore.getInstance()

    // Storage instance
    private val storageHelper: StorageHelper = StorageHelper()

    // Gets all valid deals for customer
    override fun getValidDeals(callback: ICallbackDeals, orderList: List<Order>, supplierId: String) {
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
    override fun getInvalidDeals(callback: ICallbackDeals, orderList: List<Order>, supplierId: String) {
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
    override fun getAllDeals(callback: ICallbackDeals, orderList: List<Order>, supplierId: String) {
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