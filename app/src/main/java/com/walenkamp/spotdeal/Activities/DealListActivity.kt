package com.walenkamp.spotdeal.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.walenkamp.spotdeal.Adapters.DealListAdapter
import com.walenkamp.spotdeal.BLL.DealLogic
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Interface.ICallbackDeals
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_deal_list.*

class DealListActivity : AppCompatActivity() {

    // DealLogic instance
    private val dealLogic: DealLogic = DealLogic()

    // DealListAdapter instance
    private val adapter: DealListAdapter = DealListAdapter(this)

    // SupplierId
    private lateinit var supplierId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal_list)
        setSupportActionBar(toolbar_deal_list)
        supplierId = intent.getStringExtra(SUPPLIER_ID)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.deal_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.add_deal -> {
                startCreateDeal()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        progress_deal_list.visibility = View.VISIBLE
        rec_deal_list.visibility = View.INVISIBLE
        getDeals(intent.getStringExtra(SUPPLIER_ID))
        super.onResume()
    }

    // Gets all deals for a supplier
    private fun getDeals(supplierId: String) {
        dealLogic.getAllDealsForSupplier(supplierId, object : ICallbackDeals {
            override fun onFinishDeals(deals: List<Deal>?) {
                if(deals!!.isEmpty()) {
                    status_deal_list.visibility = View.VISIBLE
                    progress_deal_list.visibility = View.INVISIBLE
                    return
                }
                status_deal_list.visibility = View.INVISIBLE
                progress_deal_list.visibility = View.INVISIBLE
                rec_deal_list.visibility = View.VISIBLE
                rec_deal_list.adapter = adapter
                rec_deal_list.layoutManager = LinearLayoutManager(baseContext)

                adapter.setDeals(deals)
            }
        })
    }

    // Start the CreateDealActivity
    private fun startCreateDeal() {
        val intent = Intent(this, CreateDealActivity::class.java).putExtra(SUPPLIER_ID, supplierId)
        startActivity(intent)
    }
}
