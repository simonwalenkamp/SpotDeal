package com.walenkamp.spotdeal.Entities

import java.io.Serializable

data class Deal(
    var id: String = "",
    var supplierId: String = "",
    var name: String = "",
    var price: Float = 0F,
    var description: String? = "",
    var info: String? = ""
) : Serializable