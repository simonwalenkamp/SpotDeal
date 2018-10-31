package com.walenkamp.spotdeal

import android.content.Intent
import android.net.Uri
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


        phone_tv.setOnClickListener{
            openDial()
        }

        email_tv.setOnClickListener {
            openMail()
        }

    }

    // Opens dial with customer phone nr
    private fun openDial(){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + phone_tv.text)
        startActivity(intent)
    }

    // Opens mail with customer mail address
    private fun openMail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:" + email_tv.text)
        startActivity(intent)
    }
}
