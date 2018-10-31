package com.walenkamp.spotdeal.Entities

import java.io.Serializable

data class Customer(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var address: String = "",
    var phone: Int = 0
) :  Serializable