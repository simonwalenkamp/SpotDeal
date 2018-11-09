package com.walenkamp.spotdeal.Activities

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import com.walenkamp.spotdeal.Adapters.OrderAdapter
import com.walenkamp.spotdeal.BLL.OrderLogic
import com.walenkamp.spotdeal.DAL.StorageHelper
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackDealImage
import com.walenkamp.spotdeal.Interface.ICallbackOrders
import com.walenkamp.spotdeal.R
import com.walenkamp.spotdeal.Separator
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.activity_deal.*

const val DEAL        = "DEAL"

class DealActivity : AppCompatActivity() {
    // Deal instance
    private lateinit var deal: Deal

    // Customer instance
    private lateinit var customer: Customer

    // Storage instance
    private var storage: StorageHelper = StorageHelper()

    // List of orders selected
    private val ordersSelected = mutableListOf<Order>()

    // List of orders shown
    private val ordersShown = mutableListOf<Order>()

    // OrderLogic intance
    private var orderLogic: OrderLogic = OrderLogic()

    // DealAdapter instance
    private val adapter = OrderAdapter(this) { order : Order -> itemClicked(order)}

    // Separator instance
    private lateinit var separator: Separator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)

        deal = intent.getSerializableExtra(DEAL) as Deal
        customer = intent.getSerializableExtra(CUSTOMER) as Customer
        toolbar_deal.title = deal.name
        setSupportActionBar(toolbar_deal)

        separator = Separator(this)
        rec_order.addItemDecoration(separator)

        deal_description.text = deal.description
        deal_info.text = deal.info
        setImage()

        getValidOrders()
        seek_bar.progress = 6
        select_all_cb.setOnClickListener {
            handleSelectAll()
        }

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("SIMON", "Start")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d("SIMON", "Start")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar?.progress!! >= 51) {
                    seekBar.progress = 94
                    updateOrders()
                } else {
                    seekBar.progress = 6
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.order_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.load_valid -> {
                getValidOrders()
                displayValid()
            }
            R.id.load_invalid -> {
                getInvalidOrders()
                displayInvalid()
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

    // Uses the StorageHelper class the get the image of the deal
    private fun setImage(){
        storage.getDealImage(object : ICallbackDealImage {
            override fun onFinishDealImage(dealImage: Bitmap?) {
                deal_image.setImageBitmap(dealImage)
            }
        }, deal.imageId!!)
    }

    // Gets the orders for the deal
    private fun getValidOrders() {
        ordersSelected.clear()
        ordersShown.clear()
        status_order_tv.visibility = View.INVISIBLE
        redeem_tv.text = getText(R.string.swipe_to_complete)
        rec_order.visibility = View.INVISIBLE
        order_progress.visibility = View.VISIBLE
        orderLogic.getValidOrdersByDeal(object : ICallbackOrders{
            override fun onFinishOrders(orders: List<Order>?) {
                if(orders!!.isEmpty()) {
                    status_order_tv.text = getText(R.string.status_no_valid)
                    status_order_tv.visibility = View.VISIBLE
                }
              rec_order.visibility = View.VISIBLE
              rec_order.adapter = adapter
              rec_order.layoutManager = LinearLayoutManager(baseContext)
              ordersShown.addAll(orders)
              adapter.setOrders(ordersShown)
              order_progress.visibility = View.INVISIBLE
            }
        }, customer.id, deal.id)
    }

    // Gets the orders for the deal
    private fun getInvalidOrders() {
        ordersSelected.clear()
        ordersShown.clear()
        status_order_tv.visibility = View.INVISIBLE
        redeem_tv.text = getText(R.string.swipe_to_reopen)
        rec_order.visibility = View.INVISIBLE
        order_progress.visibility = View.VISIBLE
        orderLogic.getInvalidOrdersByDeal(object : ICallbackOrders{
            override fun onFinishOrders(orders: List<Order>?) {
                if(orders!!.isEmpty()) {
                    status_order_tv.text = getText(R.string.status_no_invalid)
                    status_order_tv.visibility = View.VISIBLE
                }
                rec_order.visibility = View.VISIBLE
                rec_order.adapter = adapter
                rec_order.layoutManager = LinearLayoutManager(baseContext)
                ordersShown.addAll(orders)
                adapter.setOrders(ordersShown)
                order_progress.visibility = View.INVISIBLE
            }
        }, customer.id, deal.id)
    }

    // Adds the clicked order to ordersSelected
    private fun itemClicked(order: Order) {
        if(ordersSelected.contains(order)) {
            ordersSelected.remove(order)
            if(ordersSelected.size == 0) {
                layout_swipe.visibility = View.GONE
            }
            return
        }
        layout_swipe.visibility = View.VISIBLE
        ordersSelected.add(order)
    }

    // Updates orders
    private fun updateOrders(){
        layout_swipe.visibility = View.GONE
        rec_order.visibility = View.INVISIBLE
        order_progress.visibility = View.VISIBLE
        for (o in ordersSelected) {
            o.valid = !o.valid
        }
        orderLogic.updateOrders(object : ICallbackOrders {
            override fun onFinishOrders(orders: List<Order>?) {
                onBackPressed()
            }
        }, ordersSelected)

    }

    // Show valid vouchers displayed
    private fun displayValid() {
        order_status_icon.setImageDrawable(getDrawable(R.drawable.ic_valid))
        orders_shown_tv.text = getText(R.string.valid_orders)
    }

    // Show invalid displayed
    private fun displayInvalid() {
        order_status_icon.setImageDrawable(getDrawable(R.drawable.ic_invalid))
        orders_shown_tv.text = getText(R.string.invalid_orders)
    }

    // Checks or unchecks all checkboxes
    private fun handleSelectAll() {
        if(ordersShown.isEmpty()) {
            select_all_cb.isChecked = false
            return
        } else if(ordersSelected.size == ordersShown.size){
            uncheckAllBoxes()
            ordersSelected.clear()
            layout_swipe.visibility = View.GONE
            select_all_cb.isChecked = false
            return
        } else {
            ordersSelected.clear()
            ordersSelected.addAll(ordersShown)
            checkAllBoxes()
            layout_swipe.visibility = View.VISIBLE
        }
    }

    // Checks all checkboxes in recyclerView
    private fun checkAllBoxes() {
        adapter.checkAllBoxes = true
        adapter.notifyDataSetChanged()
    }

    // Unchecks all checkboxes in recyclerView
    private fun uncheckAllBoxes() {
        adapter.checkAllBoxes = false
        adapter.notifyDataSetChanged()
    }
}
