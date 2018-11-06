package com.walenkamp.spotdeal.Activities

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.walenkamp.spotdeal.Adapters.OrderAdapter
import com.walenkamp.spotdeal.BLL.OrderLogic
import com.walenkamp.spotdeal.DAL.StorageHelper
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackDealImage
import com.walenkamp.spotdeal.Interface.ICallbackOrders
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_deal.*

const val DEAL        = "DEAL"

class DealActivity : AppCompatActivity() {
    // Deal instance
    private lateinit var deal: Deal

    // Customer instance
    private lateinit var customer: Customer

    // Storage instance
    private var storage: StorageHelper = StorageHelper()

    // List of orders
    private lateinit var orders: List<Order>

    // OrderLogic intance
    private var orderLogic: OrderLogic = OrderLogic()

    // DealAdapter instance
    private val adapter = OrderAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)

        deal = intent.getSerializableExtra(DEAL) as Deal
        customer = intent.getSerializableExtra(CUSTOMER) as Customer
        toolbar_deal.title = deal.name
        setSupportActionBar(toolbar_deal)

        deal_description.text = deal.description
        deal_info.text = deal.info
        setImage()

        getOrders()
        rec_order.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                Log.d("SIMON", "pressed")

            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                Log.d("SIMON", "pressed")
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                Log.d("SIMON", "pressed")
            }
        })
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
    private fun getOrders() {
        orderLogic.getOrdersByDeal(object : ICallbackOrders{
            override fun onFinishOrders(orders: List<Order>?) {
              rec_order.adapter = adapter
              rec_order.layoutManager = LinearLayoutManager(baseContext)
              adapter.setOrders(orders!!)
              order_progress.visibility = View.INVISIBLE
            }
        }, customer.id, deal.id)
    }
}
