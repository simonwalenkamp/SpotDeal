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

    // OrderLogic instance
    private val orderLogic: OrderLogic = OrderLogic()

    // Gets all valid deals a customer has with a supplier
    fun getValidDeals(callback: ICallbackDeals, customerId: String, supplierId: String) {
        orderLogic.getOrdersByCustomer(object : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                dal.getValidDeals(object : ICallbackDeals {
                    override fun onFinishDeals(deals: List<Deal>?) {
                        val dealList = deals
                        callback.onFinishDeals(dealList)
                    }
                }, orders!!, supplierId)
            }
        }, customerId, supplierId)
    }

    // Gets invalid deals a customer has with a supplier
    fun getInvalidDeals(callback: ICallbackDeals, customerId: String, supplierId: String) {
        orderLogic.getOrdersByCustomer(object : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                dal.getInvalidDeals(object : ICallbackDeals {
                    override fun onFinishDeals(deals: List<Deal>?) {
                        val dealList = deals
                        callback.onFinishDeals(dealList)
                    }
                }, orders!!, supplierId)
            }
        }, customerId, supplierId)
    }

    // Gets all deals a customer has with a supplier
    fun getAllDeals(callback: ICallbackDeals, customerId: String, supplierId: String) {
        orderLogic.getOrdersByCustomer(object : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                dal.getAllDeals(object : ICallbackDeals {
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