package com.walenkamp.spotdeal.Entities

class Order(
    var id: String = "",
    var supplierId: String = "",
    var dealId: String = "",
    var customerId: String= "",
    var valid: Boolean = false
)