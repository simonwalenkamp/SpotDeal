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
            override fun onFinishDeal(d: Deal?) {
                callback.onFinishDeal(d)
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

    // Deletes a deal if it has no orders
    fun deleteDeal(dealId: String, callback: ICallbackFinished) {
        orderLogic.getAllOrdersByDeal(dealId, object : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                if(orders!!.isEmpty()) {
                    dealDAO.deleteDeal(dealId, object : ICallbackFinished{
                        override fun onFinishFinished(finished: Boolean) {
                            callback.onFinishFinished(finished)
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
            override fun onFinishDeal(d: Deal?) {
                storage.saveImage(img, d!!.id, object : ICallbackFinished {
                    override fun onFinishFinished(finished: Boolean) {
                        callback.onFinishFinished(true)
                    }
                })
            }
        })
    }

    // Edit a deal
    fun editDeal(d: Deal, img: Bitmap, callback: ICallbackFinished) {
        orderLogic.getAllOrdersByDeal(d.id, object : ICallbackOrders{
            override fun onFinishOrders(orders: List<Order>?) {
                if(orders!!.isEmpty()) {
                    dealDAO.editDeal(d, object : ICallbackDeal {
                        override fun onFinishDeal(d: Deal?) {
                            storage.editImage(img, d!!.id, object : ICallbackFinished{
                                override fun onFinishFinished(finished: Boolean) {
                                    callback.onFinishFinished(finished)
                                }
                            })
                        }
                    })
                } else {
                    callback.onFinishFinished(false)
                }
            }
        })

    }
}