package com.walenkamp.spotdeal.BLL

import com.walenkamp.spotdeal.DAL.CustomerDAO
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackCustomer
import com.walenkamp.spotdeal.Interface.ICallbackCustomers
import com.walenkamp.spotdeal.Interface.ICallbackOrders

class CustomerLogic {

    // Dal instance
    private val customerDAO: CustomerDAO = CustomerDAO()

    // OrderLogic instance
    private val orderLogic: OrderLogic = OrderLogic()

    // Gets all customers
    fun getCustomers(callback: ICallbackCustomers, supplierId: String) {
        orderLogic.getOrders(object  : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                customerDAO.getCustomers(object : ICallbackCustomers {
                    override fun onFinishCustomers(customers: List<Customer>?) {
                        val customerList = customers
                        callback.onFinishCustomers(customerList)
                    }
                }, orders!!)
            }
        }, supplierId)
    }

    // Gets spicific customer
    fun getCustomerById(id: String, callback: ICallbackCustomer) {
        customerDAO.getCustomerById(id, object : ICallbackCustomer{
            override fun onFinishCustomer(customer: Customer?) {
                callback.onFinishCustomer(customer)
            }
        })
    }

    //Get customers by deal
    fun getCustomerByDeal(dealId: String, callback: ICallbackCustomers) {
        orderLogic.getActiveOrdersByDeal(dealId, object : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                customerDAO.getCustomers(object : ICallbackCustomers{
                    override fun onFinishCustomers(customers: List<Customer>?) {
                        callback.onFinishCustomers(customers)
                    }
                }, orders!!)
            }
        })
    }
}