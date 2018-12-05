package com.walenkamp.spotdeal.ViewHolders

import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.walenkamp.spotdeal.Activities.CUSTOMER
import com.walenkamp.spotdeal.Activities.DEAL
import com.walenkamp.spotdeal.Activities.DealActivity
import com.walenkamp.spotdeal.Activities.SupplierDealActivity
import com.walenkamp.spotdeal.BLL.DealLogic
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Interface.ICallbackDealImage
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.deal_list_item.view.*

class DealListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // DealLogic instance
    private val dealLogic: DealLogic = DealLogic()

    // Sets the deal data on the DealItem
    fun bind(deal: Deal) {

        itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.rec_background)
        itemView.deal_name_tv.text = deal.name
        itemView.deal_description_tv.text = deal.description
        getDealImage(deal.id)
        itemView.deal_info_tv.text = deal.info
        itemView.setOnClickListener{
            val intent = Intent(itemView.context, SupplierDealActivity::class.java).putExtra(
                DEAL, deal)
            itemView.context.startActivity(intent)
        }
    }

    // Gets deal image
    private fun getDealImage(dealImageId: String) {
        dealLogic.getDealImage( object : ICallbackDealImage {
            override fun onFinishDealImage(dealImage: Bitmap?) {
                itemView.deal_image_iv.setImageBitmap(dealImage)
            }
        }, dealImageId)
    }
}