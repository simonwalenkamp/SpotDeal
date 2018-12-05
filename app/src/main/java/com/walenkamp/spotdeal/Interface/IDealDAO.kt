package com.walenkamp.spotdeal.Interface

import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Entities.Order

interface IDealDAO {
    fun getValidDeals(callback: ICallbackDeals, orderList: List<Order>, supplierId: String)
    fun getInvalidDeals(callback: ICallbackDeals, orderList: List<Order>, supplierId: String)
    fun getAllDeals(callback: ICallbackDeals, orderList: List<Order>, supplierId: String)
    fun getDealById(id: String, callback: ICallbackDeal)
    fun getAllDealsForSupplier(supplierId: String, callback: ICallbackDeals)
    fun deleteDeal(dealId: String, callback: ICallbackFinished)
    fun createDeal(deal: Deal, callback: ICallbackDeal)
}