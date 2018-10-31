package com.walenkamp.spotdeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.walenkamp.spotdeal.Entities.Customer
import kotlinx.android.synthetic.main.activity_customer.*

const val CUSTOMER_ID        = "CUSTOMER_ID"

class CustomerActivity : AppCompatActivity() {

    private lateinit var customer: Customer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        customer = intent.getSerializableExtra(CUSTOMER_ID) as Customer

        toolbar_customer.title = customer.name
        email_tv.text = customer.email
        phone_tv.text = customer.phone.toString()
        address_tv.text = customer.address
    }
}
