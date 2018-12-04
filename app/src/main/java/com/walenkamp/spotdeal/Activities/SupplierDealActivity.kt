package com.walenkamp.spotdeal.Activities

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.menu.MenuBuilder
import com.google.android.material.snackbar.Snackbar
import com.walenkamp.spotdeal.BLL.DealLogic
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Interface.ICallbackCouldDelete
import com.walenkamp.spotdeal.Interface.ICallbackDealImage
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_supplier_deal.*
import kotlin.math.log

class SupplierDealActivity : AppCompatActivity() {

    // DealLogic instance
    private val dealLogic: DealLogic = DealLogic()

    // Deal instance
    private lateinit var deal: Deal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_deal)

        deal = intent.getSerializableExtra(DEAL) as Deal
        setSupportActionBar(toolbar_supplier_deal)
        toolbar_supplier_deal.title = deal.name

        setDeal(deal)
    }

    // Sets the deal
    private fun setDeal(deal: Deal) {
        supplier_deal_description.text = deal.description
        supplier_deal_info.text = deal.info
        dealLogic.getDealImage(object : ICallbackDealImage {
            override fun onFinishDealImage(dealImage: Bitmap?) {
                supplier_deal_image.setImageBitmap(dealImage)
            }
        }, deal.imageId!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.supplier_deal_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.edit_deal -> {
                Log.d("SIMON", "edit")
            }
            R.id.delete_deal -> {
                deleteDeal(deal.id)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Deletes deal
    private fun deleteDeal(id: String) {
        dealLogic.deleteDeal(id, object : ICallbackCouldDelete {
            override fun onFinishCouldDelete(couldDelete: Boolean) {
                if(couldDelete) {
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
}
