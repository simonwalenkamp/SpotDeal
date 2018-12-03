package com.walenkamp.spotdeal.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.walenkamp.spotdeal.BLL.DealLogic
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Interface.ICallbackDeals
import com.walenkamp.spotdeal.R

class DealListActivity : AppCompatActivity() {

    // DealLogic instance
    private val dealLogic: DealLogic = DealLogic()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal_list)

        getDeals(intent.getStringExtra(SUPPLIER_ID))
    }

    // Gets all deals for a supplier
    private fun getDeals(supplierId: String) {
        dealLogic.getAllDealsForSupplier(supplierId, object : ICallbackDeals {
            override fun onFinishDeals(deals: List<Deal>?) {
                for (deal in deals!!) {
                    Log.d("SIMON", deal.name)
                }
            }
        })
    }
}
