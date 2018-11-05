package com.walenkamp.spotdeal.BLL

import com.walenkamp.spotdeal.DAL.DatabaseHelper
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackCustomers
import com.walenkamp.spotdeal.Interface.ICallbackOrders

class CustomerLogic {

    // Dal instance
    private val dal: DatabaseHelper = DatabaseHelper()

    // OrderLogic instance
    private val orderLogic: OrderLogic = OrderLogic()

    // Gets all customers
    fun getCustomers(callback: ICallbackCustomers, supplierId: String) {
        orderLogic.getOrders(object  : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                dal.getCustomers(object : ICallbackCustomers {
                    override fun onFinishCustomers(customers: List<Customer>?) {
                        val customerList = customers
                        callback.onFinishCustomers(customerList)
                    }
                }, orders!!)
            }
        }, supplierId)
    }
}