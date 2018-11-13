package com.walenkamp.spotdeal.BLL

import android.content.Context
import com.walenkamp.spotdeal.DAL.LocalOrderDAO
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackOrders

class HistoryLogic(c: Context) {

    // LocalOrderDAO instance
    private val  localOrderDAO: LocalOrderDAO = LocalOrderDAO(c)

    // Get the order history
    fun getHistory(supplierId: String, callback: ICallbackOrders) {
        localOrderDAO.getAllSavedOrdersBySupplierId(supplierId, object : ICallbackOrders{
            override fun onFinishOrders(orders: List<Order>?) {
                callback.onFinishOrders(orders)
            }

        })
    }

    // Saves a list of orders
    fun saveOrders(orderList: List<Order>) {
        localOrderDAO.saveOrders(orderList)
    }
}