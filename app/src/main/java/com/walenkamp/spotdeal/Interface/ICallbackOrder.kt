package com.walenkamp.spotdeal.Interface

import com.walenkamp.spotdeal.Entities.Order

interface ICallbackOrder {
    fun onFinishOrder(order: Order?)
}