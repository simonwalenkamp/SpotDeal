package com.walenkamp.spotdeal.BLL

import android.graphics.Bitmap
import com.walenkamp.spotdeal.DAL.DealDAO
import com.walenkamp.spotdeal.DAL.StorageHelper
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.*

class DealLogic {

    // Dal instance
    private val dealDAO: DealDAO = DealDAO()

    // Storage instance
    private val storage: StorageHelper = StorageHelper()

    // OrderLogic instance
    private val orderLogic: OrderLogic = OrderLogic()

    // Gets all valid deals a customer has with a supplier
    fun getValidDealsForCustomer(callback: ICallbackDeals, customerId: String, supplierId: String) {
        orderLogic.getOrdersByCustomer(object : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                dealDAO.getValidDeals(object : ICallbackDeals {
                    override fun onFinishDeals(deals: List<Deal>?) {
                        val dealList = deals
                        callback.onFinishDeals(dealList)
                    }
                }, orders!!, supplierId)
            }
        }, customerId, supplierId)
    }

    // Gets invalid deals a customer has with a supplier
    fun getInvalidDealsForCustomer(callback: ICallbackDeals, customerId: String, supplierId: String) {
        orderLogic.getOrdersByCustomer(object : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                dealDAO.getInvalidDeals(object : ICallbackDeals {
                    override fun onFinishDeals(deals: List<Deal>?) {
                        val dealList = deals
                        callback.onFinishDeals(dealList)
                    }
                }, orders!!, supplierId)
            }
        }, customerId, supplierId)
    }

    // Gets all deals a customer has with a supplier
    fun getAllDealsForCustomer(callback: ICallbackDeals, customerId: String, supplierId: String) {
        orderLogic.getOrdersByCustomer(object : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                dealDAO.getAllDeals(object : ICallbackDeals {
                    override fun onFinishDeals(deals: List<Deal>?) {
                        val dealList = deals
                        callback.onFinishDeals(dealList)
                    }
                }, orders!!, supplierId)
            }
        }, customerId, supplierId)
    }

    // Gets deal image
    fun getDealImage(callback: ICallbackDealImage, id: String) {
        storage.getDealImage(object : ICallbackDealImage {
            override fun onFinishDealImage(dealImage: Bitmap?) {
                callback.onFinishDealImage(dealImage)
            }
        }, id)
    }

    // Gets specific deal
    fun getDealById(id: String, callback: ICallbackDeal) {
        dealDAO.getDealById(id, object : ICallbackDeal{
            override fun onFinishDeal(deal: Deal?) {
                callback.onFinishDeal(deal)
            }
        })
    }

    // Gets all deals for supplier
    fun getAllDealsForSupplier(supplierId: String, callback: ICallbackDeals) {
        dealDAO.getAllDealsForSupplier(supplierId, object : ICallbackDeals {
            override fun onFinishDeals(deals: List<Deal>?) {
                callback.onFinishDeals(deals)
            }
        })
    }

    // Deletes a deal if it has no active orders
    fun deleteDeal(dealId: String, callback: ICallbackFinished) {
        orderLogic.getActiveOrdersByDeal(dealId, object : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                if(orders!!.isEmpty()) {
                    dealDAO.deleteDeal(dealId, object : ICallbackFinished{
                        override fun onFinishFinished(couldDelete: Boolean) {
                            callback.onFinishFinished(couldDelete)
                            storage.deleteImage(dealId)
                        }
                    })
                } else {
                    callback.onFinishFinished(false)
                }
            }
        })
    }

    // Creates new deal
    fun createDeal(d: Deal, img: Bitmap, callback: ICallbackFinished) {
        dealDAO.createDeal(d, object : ICallbackDeal {
            override fun onFinishDeal(deal: Deal?) {
                storage.saveImage(img, deal!!.id, object : ICallbackFinished {
                    override fun onFinishFinished(couldDelete: Boolean) {
                        callback.onFinishFinished(true)
                    }
                })
            }
        })
    }
}