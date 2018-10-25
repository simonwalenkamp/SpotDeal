package com.walenkamp.spotdeal.Interface

import com.walenkamp.spotdeal.Entities.Customer

interface ICallbackCustomers {
    fun onFinishCustomers(customers: List<Customer>?)
}