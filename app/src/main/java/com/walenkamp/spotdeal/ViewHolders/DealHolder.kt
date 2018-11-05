package com.walenkamp.spotdeal.ViewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.walenkamp.spotdeal.BLL.DealLogic
import com.walenkamp.spotdeal.Entities.Deal
import kotlinx.android.synthetic.main.deal_list_item.view.*

class DealHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // DealLogic instance
    private val dealLogic: DealLogic = DealLogic()


    // Sets the deal data on the DealItem
    fun bind(deal: Deal) {
        itemView.deal_name_tv.text = deal.name
        itemView.deal_description_tv.text = deal.description
        itemView.deal_info_tv.text = deal.info
    }
}