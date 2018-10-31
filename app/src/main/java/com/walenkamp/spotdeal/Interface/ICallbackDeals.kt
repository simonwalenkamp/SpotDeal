package com.walenkamp.spotdeal.Interface

import com.walenkamp.spotdeal.Entities.Deal

interface ICallbackDeals {
    fun onFinishDeals(deals: List<Deal>?)
}