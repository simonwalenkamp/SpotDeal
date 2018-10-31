package com.walenkamp.spotdeal.BLL

import android.graphics.Bitmap
import com.walenkamp.spotdeal.DAL.DatabaseHelper
import com.walenkamp.spotdeal.DAL.StorageHelper
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackDealImage
import com.walenkamp.spotdeal.Interface.ICallbackDeals
import com.walenkamp.spotdeal.Interface.ICallbackOrders

class DealLogic {

    // Dal instance
    private val dal: DatabaseHelper = DatabaseHelper()

    // Storage instance
    private val storage: StorageHelper = StorageHelper()

    // Gets all deal a customer has with a supplier
    fun getDeals(callback: ICallbackDeals, customerId: String, supplierId: String) {
        dal.getOrdersByCustomer(object : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                dal.getDeals(object : ICallbackDeals {
                    override fun onFinishDeals(deals: List<Deal>?) {
                        val dealList = deals
                        callback.onFinishDeals(dealList)
                    }
                }, orders!!, supplierId)
            }
        }, customerId, supplierId)
    }

    // Gets deal image
    fun getDealImage(callback: ICallbackDealImage, dealImageId: String) {
        storage.getDealImage(object : ICallbackDealImage {
            override fun onFinishDealImage(dealImage: Bitmap?) {
                callback.onFinishDealImage(dealImage)
            }
        }, dealImageId)
    }
}