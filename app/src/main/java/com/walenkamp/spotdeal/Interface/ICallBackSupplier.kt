package com.walenkamp.spotdeal.Interface

import com.walenkamp.spotdeal.Entities.Supplier

interface ICallBackSupplier {
    fun onFinishSupplier(sup: Supplier?)
}