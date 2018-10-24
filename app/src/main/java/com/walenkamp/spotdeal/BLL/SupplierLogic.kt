package com.walenkamp.spotdeal.BLL

import com.walenkamp.spotdeal.DAL.DatabaseHelper
import com.walenkamp.spotdeal.Entities.Supplier
import com.walenkamp.spotdeal.Interface.ICallBackSupplier

class SupplierLogic {

    // Dal instance
    private val dal: DatabaseHelper = DatabaseHelper()

    // Gets the supplier
    fun getSupplier(callback: ICallBackSupplier) {
        dal.getSupplier(object : ICallBackSupplier {
            override fun onFinishSupplier(sup: Supplier?) {
                val supplier = sup!!
                callback.onFinishSupplier(supplier)
            }
        })
    }
}