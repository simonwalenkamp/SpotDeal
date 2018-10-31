package com.walenkamp.spotdeal.Entities

data class Deal(
    var id: String = "",
    var supplierId: String = "",
    var name: String = "",
    var price: Int = 0,
    var imageId: String? = "",
    var description: String? = "",
    var info: String? = "")