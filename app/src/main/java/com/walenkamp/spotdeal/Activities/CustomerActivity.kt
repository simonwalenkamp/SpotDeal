package com.walenkamp.spotdeal.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
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
import com.walenkamp.spotdeal.Separator


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
        setSupportActionBar(toolbar_customer)

        email_tv.text = customer.email
        phone_tv.text = customer.phone.toString()
        address_tv.text = customer.address

        phone_tv.setOnClickListener{
            openDial()
        }

        email_tv.setOnClickListener {
            openMail()
        }

        // Gets the supplier from SupplierLogic and calls the getValidDeals function
        supplierLogic.getSupplier(object : ICallBackSupplier {
            override fun onFinishSupplier(sup: Supplier?) {
                supplier = sup!!
            }
        })
    }

    override fun onResume() {
        // Gets the supplier from SupplierLogic and calls the getValidDeals function
        displayActive()
        supplierLogic.getSupplier(object : ICallBackSupplier {
            override fun onFinishSupplier(sup: Supplier?) {
                supplier = sup!!
                getValidDeals(customer.id ,supplier.id)
            }
        })
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.deal_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.load_active -> {
                getValidDeals(customer.id, supplier.id)
                displayActive()
            }
            R.id.load_inactive -> {
                getInvalidDeals(customer.id, supplier.id)
                displayInactive()
            }
            R.id.load_all -> {
                getAllDeals(customer.id, supplier.id)
                displayAll()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            if (menu is MenuBuilder) {
                try {
                    val field = menu.javaClass.getDeclaredField("mOptionalIconsVisible")
                    field.isAccessible = true
                    field.setBoolean(menu, true)
                } catch (ignored: Exception) {
                    // ignored exception
                }
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    // Gets all valid deals a customer has with the supplier
    private fun getValidDeals(customerId: String, supplierId: String) {
        rec_deal.visibility = View.INVISIBLE
        deal_progress.visibility = View.VISIBLE
        dealLogic.getValidDealsForCustomer(object : ICallbackDeals {
            override fun onFinishDeals(deals: List<Deal>?) {
                if(deals!!.isEmpty()) {
                    Snackbar.make(customer_constraint, R.string.status_no_active, Snackbar.LENGTH_LONG).show()
                    getInvalidDeals(customerId, supplierId)
                    displayInactive()
                }
                rec_deal.visibility = View.VISIBLE
                adapter.customer = customer
                rec_deal.adapter = adapter
                rec_deal.layoutManager = LinearLayoutManager(baseContext)
                adapter.setDeals(deals)
                deal_progress.visibility = View.INVISIBLE
            }

        }, customerId, supplierId)
    }

    // Gets all deals a customer has with the supplier
    private fun getAllDeals(customerId: String, supplierId: String) {
        rec_deal.visibility = View.INVISIBLE
        deal_progress.visibility = View.VISIBLE
        dealLogic.getAllDealsForCustomer(object : ICallbackDeals {
            override fun onFinishDeals(deals: List<Deal>?) {
                rec_deal.visibility = View.VISIBLE
                rec_deal.adapter = adapter
                rec_deal.layoutManager = LinearLayoutManager(baseContext)
                adapter.setDeals(deals!!)
                deal_progress.visibility = View.INVISIBLE
            }
        }, customerId, supplierId)
    }

    // Gets all invalid deals a customer has with the supplier
    private fun getInvalidDeals(customerId: String, supplierId: String) {
        rec_deal.visibility = View.INVISIBLE
        deal_progress.visibility = View.VISIBLE
        dealLogic.getInvalidDealsForCustomer(object : ICallbackDeals {
            override fun onFinishDeals(deals: List<Deal>?) {
                if(deals!!.isEmpty()) {
                    Snackbar.make(customer_constraint, R.string.status_no_inactive ,Snackbar.LENGTH_LONG).show()
                    getValidDeals(customerId, supplierId)
                    displayActive()
                }
                rec_deal.visibility = View.VISIBLE
                rec_deal.adapter = adapter
                rec_deal.layoutManager = LinearLayoutManager(baseContext)
                adapter.setDeals(deals)
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

    // Displays active chosen
    private fun displayActive(){
        status_icon.setImageDrawable(getDrawable(R.drawable.ic_valid))
        shown_tv.text = getText(R.string.active_deals)
    }

    // Displays inactive chosen
    private fun displayInactive(){
        status_icon.setImageDrawable(getDrawable(R.drawable.ic_invalid))
        shown_tv.text = getText(R.string.inactive_deals)
    }

    // Displays all chosen
    private fun displayAll(){
        status_icon.setImageDrawable(getDrawable(R.drawable.ic_all))
        shown_tv.text = getText(R.string.all_deals)
    }
}
