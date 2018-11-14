package com.walenkamp.spotdeal.Interface

import com.walenkamp.spotdeal.Entities.Order

interface ICustomerDAO {
    fun getCustomers(callback: ICallbackCustomers, orderList: List<Order>)
    fun getCustomerById(id: String, callback: ICallbackCustomer)
}