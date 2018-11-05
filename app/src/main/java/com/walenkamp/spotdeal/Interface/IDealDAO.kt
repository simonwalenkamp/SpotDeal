package com.walenkamp.spotdeal.Interface

import com.walenkamp.spotdeal.Entities.Order

interface IDealDAO {
    fun getValidDeals(callback: ICallbackDeals, orderList: List<Order>, supplierId: String)
    fun getInvalidDeals(callback: ICallbackDeals, orderList: List<Order>, supplierId: String)
    fun getAllDeals(callback: ICallbackDeals, orderList: List<Order>, supplierId: String)
}