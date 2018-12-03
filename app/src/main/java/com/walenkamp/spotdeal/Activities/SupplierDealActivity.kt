package com.walenkamp.spotdeal.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_supplier_deal.*

class SupplierDealActivity : AppCompatActivity() {

    // Deal instance
    private lateinit var deal: Deal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_deal)

        deal = intent.getSerializableExtra(DEAL) as Deal

        toolbar_supplier_deal.title = deal.name
    }
}
