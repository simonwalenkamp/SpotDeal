package com.walenkamp.spotdeal.BLL

import com.walenkamp.spotdeal.DAL.DatabaseHelper
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Interface.ICallbackCustomers

class CustomerLogic {

    // Dal instance
    private val dal: DatabaseHelper = DatabaseHelper()

    // Gets all customers
    fun getCustomers(callback: ICallbackCustomers) {
      dal.getCustomers(object : ICallbackCustomers {
          override fun onFinishCustomers(customers: List<Customer>?) {
              val customerList = customers
              callback.onFinishCustomers(customerList)
          }
      })
    }
}