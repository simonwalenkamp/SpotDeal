package com.walenkamp.spotdeal.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.walenkamp.spotdeal.BLL.HistoryLogic
import com.walenkamp.spotdeal.BLL.SupplierLogic
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.R

const val SUPPLIER_ID        = "SUPPLIER_ID"

class OrderHistoryActivity : AppCompatActivity() {

    // HistoryLogic instance
    private lateinit var historyLogic: HistoryLogic

    // SupplierId
    private var supplierId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        // init instance
        historyLogic = HistoryLogic(this)

        supplierId = intent.getSerializableExtra(SUPPLIER_ID) as String

    }
}
