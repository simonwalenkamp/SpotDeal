package com.walenkamp.spotdeal.Interface

interface IOrderDAO {
    fun getOrders(callback: ICallbackOrders, supplierId: String)
    fun getOrdersByCustomer(callback: ICallbackOrders, customerId: String, supplierId: String)
}