package com.walenkamp.spotdeal.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.walenkamp.spotdeal.Adapters.HistoryAdapter
import com.walenkamp.spotdeal.BLL.HistoryLogic
import com.walenkamp.spotdeal.BLL.SupplierLogic
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackOrders
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_deal.*
import kotlinx.android.synthetic.main.activity_order_history.*

const val SUPPLIER_ID        = "SUPPLIER_ID"

class OrderHistoryActivity : AppCompatActivity() {

    // HistoryLogic instance
    private lateinit var historyLogic: HistoryLogic

    // SupplierId
    private var supplierId: String = ""

    // DealAdapter instance
    private val adapter = HistoryAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        // init instance
        historyLogic = HistoryLogic(this)

        supplierId = intent.getSerializableExtra(SUPPLIER_ID) as String

        getHistory()
    }

    override fun onResume() {
        super.onResume()
        getHistory()
    }

    // Gets history and sets them to the recyclerView
    private fun getHistory() {
        history_progress.visibility = View.VISIBLE
        historyLogic.getHistory(supplierId, object : ICallbackOrders{
            override fun onFinishOrders(orders: List<Order>?) {
                history_progress.visibility = View.INVISIBLE
                rec_history.adapter = adapter
                rec_history.layoutManager = LinearLayoutManager(baseContext)
                adapter.setOrders(orders!!)
                rec_history.layoutManager = LinearLayoutManager(baseContext)
            }
        })
    }
}
