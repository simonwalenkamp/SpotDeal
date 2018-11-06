package com.walenkamp.spotdeal.BLL

import com.walenkamp.spotdeal.DAL.OrderDAO
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackOrders

class OrderLogic {
    // Dal instance
    private val orderDAO: OrderDAO = OrderDAO()

    // Uses the orderDAO to a specific customers orders
    fun getOrdersByCustomer(callback: ICallbackOrders, customerId: String, supplierId: String){
        orderDAO.getOrdersByCustomer(object : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                callback.onFinishOrders(orders)
            }
        }, customerId, supplierId)
    }

    // Uses the orderDAO to get orders by supplier
    fun getOrders(callback: ICallbackOrders, supplierId: String) {
        orderDAO.getOrders(object : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                callback.onFinishOrders(orders)
            }
        }, supplierId)
    }

    // Gets order by customer and deal
    fun getOrdersByDeal(callback: ICallbackOrders, customerId: String, dealId: String){
        orderDAO.getOrdersByDeal(object : ICallbackOrders{
            override fun onFinishOrders(orders: List<Order>?) {
                callback.onFinishOrders(orders)
            }
        }, customerId, dealId)
    }

    // Updates orders
    fun updateOrders(callback: ICallbackOrders, orders: List<Order>) {
        orderDAO.updateOrders(object : ICallbackOrders{
            override fun onFinishOrders(orders: List<Order>?) {
                callback.onFinishOrders(orders)
            }
        }, orders)
    }
}