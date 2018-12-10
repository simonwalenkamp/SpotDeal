package com.walenkamp.spotdeal.Interface

import com.walenkamp.spotdeal.Entities.Deal

interface ICallbackDeal {
    fun onFinishDeal(d: Deal?)
}