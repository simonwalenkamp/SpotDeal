package com.walenkamp.spotdeal.Activities

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.walenkamp.spotdeal.Adapters.CustomerAdapter
import com.walenkamp.spotdeal.BLL.CustomerLogic
import com.walenkamp.spotdeal.BLL.DealLogic
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Interface.ICallbackFinished
import com.walenkamp.spotdeal.Interface.ICallbackCustomers
import com.walenkamp.spotdeal.Interface.ICallbackDeal
import com.walenkamp.spotdeal.Interface.ICallbackDealImage
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_supplier_deal.*

class SupplierDealActivity : AppCompatActivity() {

    // CustomerLogic
    private val customerLogic: CustomerLogic = CustomerLogic()

    // DealLogic instance
    private val dealLogic: DealLogic = DealLogic()

    // Deal instance
    private lateinit var deal: Deal

    // CustomerAdapter instance
    private val adapter: CustomerAdapter = CustomerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_deal)

        deal = intent.getSerializableExtra(DEAL) as Deal
        toolbar_supplier_deal.title = deal.name

        setSupportActionBar(toolbar_supplier_deal)

        setDeal(deal)
        getCustomers(deal.id)
    }

    override fun onRestart() {
        super.onRestart()

        hideLayout()
        getDeal()
    }

    // Sets the deal
    private fun setDeal(deal: Deal) {
        supplier_deal_description.text = deal.description
        supplier_deal_info.text = deal.info
        dealLogic.getDealImage(object : ICallbackDealImage {
            override fun onFinishDealImage(dealImage: Bitmap?) {
                supplier_deal_image.setImageBitmap(dealImage)
            }
        }, deal.id)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.supplier_deal_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.edit_deal -> {
                startEditDeal()
            }
            R.id.delete_deal -> {
                deleteDeal(deal.id)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Starts the EditDealActivity
    private fun startEditDeal() {
        val intent = Intent(this, EditDealActivity::class.java).putExtra(DEAL, deal)
        startActivity(intent)
    }

    // Deletes deal
    private fun deleteDeal(id: String) {
        dealLogic.deleteDeal(id, object : ICallbackFinished {
            override fun onFinishFinished(finished: Boolean) {
                if(finished) {
                    finish()
                } else {
                    Snackbar.make(supplier_deal_constraint, "Could not delete... Check if the deal has active orders!", Snackbar.LENGTH_LONG).show()
                }
            }
        })
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

    // Gets customers who have an order on this deal
    private fun getCustomers(dealId: String){
        customerLogic.getCustomerByDeal(dealId, object : ICallbackCustomers {
            override fun onFinishCustomers(customers: List<Customer>?) {
                if(customers!!.isEmpty()) {
                    Snackbar.make(supplier_deal_constraint, "Deal has no active vouchers!", Snackbar.LENGTH_LONG).show()
                }
                progress_supplier_deal.visibility = View.INVISIBLE
                rec_supplier_deal.visibility = View.VISIBLE
                rec_supplier_deal.adapter = adapter
                rec_supplier_deal.layoutManager = LinearLayoutManager(baseContext)

                adapter.setCustomers(customers)
            }
        })
    }

    // Shows layout
    private fun showLayout() {
        supplier_deal_image.visibility = View.VISIBLE
        supplier_deal_info.visibility = View.VISIBLE
        supplier_deal_description.visibility = View.VISIBLE
        toolbar_supplier_deal.visibility = View.VISIBLE
        rec_supplier_deal.visibility = View.VISIBLE
        supplier_deal_separator.visibility = View.VISIBLE
        supplier_deal_info_icon.visibility = View.VISIBLE

        progress_supplier_deal_deal.visibility = View.INVISIBLE
    }

    // Hides layout to show progress
    private fun hideLayout() {
        supplier_deal_image.visibility = View.INVISIBLE
        supplier_deal_info.visibility = View.INVISIBLE
        supplier_deal_description.visibility = View.INVISIBLE
        toolbar_supplier_deal.visibility = View.INVISIBLE
        rec_supplier_deal.visibility = View.INVISIBLE
        supplier_deal_separator.visibility = View.INVISIBLE
        supplier_deal_info_icon.visibility = View.INVISIBLE


        progress_supplier_deal_deal.visibility = View.VISIBLE
    }

    // Gets updated deal
    private fun getDeal() {
        dealLogic.getDealById(deal.id, object : ICallbackDeal{
            override fun onFinishDeal(deal: Deal?) {
                setDeal(deal!!)
                dealLogic.getDealImage(object : ICallbackDealImage{
                    override fun onFinishDealImage(dealImage: Bitmap?) {
                        supplier_deal_image.setImageBitmap(dealImage)
                        showLayout()
                    }
                }, deal.id)
            }
        })
    }
}
