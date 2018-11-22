package com.walenkamp.spotdeal.BLL

import com.walenkamp.spotdeal.DAL.OrderDAO
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackOrder
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

    // Gets valid orders by customer and deal
    fun getValidOrdersByDeal(callback: ICallbackOrders, customerId: String, dealId: String){
        orderDAO.getValidOrdersByDeal(object : ICallbackOrders{
            override fun onFinishOrders(orders: List<Order>?) {
                callback.onFinishOrders(orders)
            }
        }, customerId, dealId)
    }

    // Gets invalid orders by customer and deal
    fun getInvalidOrdersByDeal(callback: ICallbackOrders, customerId: String, dealId: String){
        orderDAO.getInvalidOrdersByDeal(object : ICallbackOrders{
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

    // Gets order by id
    fun getOrderById(id: String, callback: ICallbackOrder) {
        orderDAO.getOrderById(id, object : ICallbackOrder {
            override fun onFinishOrder(order: Order?) {
                callback.onFinishOrder(order)
            }
        })
    }
}