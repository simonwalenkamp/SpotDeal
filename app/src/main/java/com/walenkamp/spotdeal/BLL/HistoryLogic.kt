package com.walenkamp.spotdeal.BLL

import android.content.Context
import com.walenkamp.spotdeal.DAL.LocalOrderDAO
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackDone
import com.walenkamp.spotdeal.Interface.ICallbackOrders

class HistoryLogic(c: Context) {

    private val limit = 50

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
    // if what it is trying to save extends the limit deletes the amount from the first records in database
    fun saveOrders(orderList: List<Order>) {
        getHistory(orderList[0].supplierId, object : ICallbackOrders{
            override fun onFinishOrders(orders: List<Order>?) {
                if(orderList.size + orders!!.size > limit) {
                    localOrderDAO.deleteRows(orderList.size + orders.size - limit, object : ICallbackDone {
                        override fun onFinishTask(done: String) {
                            localOrderDAO.saveOrders(orderList)
                        }
                    })
                } else {
                    localOrderDAO.saveOrders(orderList)
                }
            }
        })
    }
}