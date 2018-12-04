package com.walenkamp.spotdeal.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.walenkamp.spotdeal.Adapters.DealListAdapter
import com.walenkamp.spotdeal.BLL.DealLogic
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Interface.ICallbackDeals
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.activity_deal_list.*

class DealListActivity : AppCompatActivity() {

    // DealLogic instance
    private val dealLogic: DealLogic = DealLogic()

    // DealListAdapter instance
    private val adapter: DealListAdapter = DealListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal_list)

    }

    override fun onResume() {
        progress_deal_list.visibility = View.VISIBLE
        getDeals(intent.getStringExtra(SUPPLIER_ID))
        super.onResume()
    }

    // Gets all deals for a supplier
    private fun getDeals(supplierId: String) {
        dealLogic.getAllDealsForSupplier(supplierId, object : ICallbackDeals {
            override fun onFinishDeals(deals: List<Deal>?) {
                progress_deal_list.visibility = View.INVISIBLE
                rec_deal_list.adapter = adapter
                rec_deal_list.layoutManager = LinearLayoutManager(baseContext)

                adapter.setDeals(deals!!)
            }
        })
    }
}
