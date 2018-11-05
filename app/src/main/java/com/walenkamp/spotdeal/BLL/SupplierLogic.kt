package com.walenkamp.spotdeal.BLL

import com.walenkamp.spotdeal.DAL.SupplierDAO
import com.walenkamp.spotdeal.Entities.Supplier
import com.walenkamp.spotdeal.Interface.ICallBackSupplier

class SupplierLogic {

    // Dal instance
    private val supplierDAO: SupplierDAO = SupplierDAO()

    // Gets the supplier
    fun getSupplier(callback: ICallBackSupplier) {
        supplierDAO.getSupplier(object : ICallBackSupplier {
            override fun onFinishSupplier(sup: Supplier?) {
                val supplier = sup!!
                callback.onFinishSupplier(supplier)
            }
        })
    }
}