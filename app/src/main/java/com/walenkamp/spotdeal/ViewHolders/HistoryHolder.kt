package com.walenkamp.spotdeal.ViewHolders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.history_list_item.view.*
import kotlinx.android.synthetic.main.order_list_item.view.*

class HistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    //Sets the order data on the HistoryItem
    fun bind(order: Order) {
        itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.rec_background)

        if(order.valid) {
            itemView.history_status.text = itemView.resources.getText(R.string.valid_voucher)
            itemView.history_status.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorGreen))
        } else {
            itemView.history_status.text = itemView.resources.getText(R.string.invalid_voucher)
            itemView.history_status.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorRed))
        }

        itemView.history_voucher.text = order.id
    }
}