package com.walenkamp.spotdeal.Interface

import com.walenkamp.spotdeal.Entities.Customer

interface ICallbackCustomer {
    fun onFinishCustomer(customer: Customer?)
}