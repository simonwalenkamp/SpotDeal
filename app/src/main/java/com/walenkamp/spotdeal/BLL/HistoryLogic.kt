package com.walenkamp.spotdeal.BLL

import android.content.Context
import com.walenkamp.spotdeal.DAL.LocalOrderDAO
import com.walenkamp.spotdeal.Entities.Order

class HistoryLogic(c: Context) {

    // LocalOrderDAO instance
    private val  localOrderDAO: LocalOrderDAO = LocalOrderDAO(c)

    // Get the order history
    fun getHistory(): List<Order>{
        return localOrderDAO.getAllSavedOrders()
    }

    // Saves a list of orders
    fun saveOrders(orders: List<Order>) {
        localOrderDAO.saveOrders(orders)
    }
}