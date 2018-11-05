package com.walenkamp.spotdeal.Activities

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.walenkamp.spotdeal.DAL.StorageHelper
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Interface.ICallbackDealImage
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_deal.*
import kotlinx.android.synthetic.main.deal_list_item.*

const val DEAL        = "DEAL"

class DealActivity : AppCompatActivity() {
    // Deal instance
    private lateinit var deal: Deal

    // Storage instance
    private var storage: StorageHelper = StorageHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)

        deal = intent.getSerializableExtra(DEAL) as Deal
        toolbar_deal.title = deal.name
        setSupportActionBar(toolbar_deal)

        deal_description.text = deal.description
        deal_info.text = deal.info
        setImage()
    }

    private fun setImage(){
        storage.getDealImage(object : ICallbackDealImage {
            override fun onFinishDealImage(dealImage: Bitmap?) {
                deal_image.setImageBitmap(dealImage)
            }
        }, deal.imageId!!)
    }
}
