package com.walenkamp.spotdeal.Interface

import com.walenkamp.spotdeal.Entities.Order

interface IOrderDAO {
    fun getOrders(callback: ICallbackOrders, supplierId: String)
    fun getOrdersByCustomer(callback: ICallbackOrders, customerId: String, supplierId: String)
    fun getValidOrdersByDeal(callback: ICallbackOrders, customerId: String, dealId: String)
    fun getInvalidOrdersByDeal(callback: ICallbackOrders, customerId: String, dealId: String)
    fun updateOrders(callback: ICallbackOrders, orders: List<Order>)
    fun getOrderById(id: String, callback: ICallbackOrder)
}