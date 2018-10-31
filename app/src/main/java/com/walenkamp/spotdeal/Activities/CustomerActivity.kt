package com.walenkamp.spotdeal.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.walenkamp.spotdeal.Adapters.DealAdapter
import com.walenkamp.spotdeal.BLL.DealLogic
import com.walenkamp.spotdeal.BLL.SupplierLogic
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Entities.Supplier
import com.walenkamp.spotdeal.Interface.ICallBackSupplier
import com.walenkamp.spotdeal.Interface.ICallbackDeals
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_customer.*

const val CUSTOMER        = "CUSTOMER"

class CustomerActivity : AppCompatActivity() {

    // Customer instance
    private lateinit var customer: Customer

    // Supplier instance
    private lateinit var supplier: Supplier

    // SupplierLogic instance
    private val supplierLogic: SupplierLogic = SupplierLogic()

    // DealLogic instance
    private val dealLogic: DealLogic = DealLogic()

    // DealAdapter instance
    private val adapter = DealAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        customer = intent.getSerializableExtra(CUSTOMER) as Customer

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

        // Gets the supplier from SupplierLogic and calls the getCustomers function
        supplierLogic.getSupplier(object : ICallBackSupplier {
            override fun onFinishSupplier(sup: Supplier?) {
                supplier = sup!!
                getDeals(customer.id ,supplier.id)
            }
        })
    }

    // Gets all deals a customer has with the supplier
    private fun getDeals(customerId: String, supplierId: String) {
        dealLogic.getDeals(object : ICallbackDeals {
            override fun onFinishDeals(deals: List<Deal>?) {
                rec_deal.adapter = adapter
                rec_deal.layoutManager = LinearLayoutManager(baseContext)
                adapter.setDeals(deals!!)
                deal_progress.visibility = View.INVISIBLE
            }

        }, customerId, supplierId)
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
