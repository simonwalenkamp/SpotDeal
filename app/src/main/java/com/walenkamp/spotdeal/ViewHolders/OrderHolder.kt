package com.walenkamp.spotdeal.ViewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.order_list_item.view.*

class OrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // Sets the deal data on the DealItem
    fun bind(order: Order, clickListener: (Order) -> Unit) {
        if(order.valid) {
            itemView.status.text = itemView.resources.getText(R.string.valid_voucher)
            itemView.status.setTextColor(itemView.resources.getColor(R.color.colorGreen))
        } else {
            itemView.status.text = itemView.resources.getText(R.string.invalid_voucher)
            itemView.status.setTextColor(itemView.resources.getColor(R.color.colorRed))
        }

        itemView.setOnClickListener {
            itemView.checkbox.isChecked = !itemView.checkbox.isChecked
            clickListener(order)
        }
        itemView.checkbox.setOnClickListener {
            clickListener(order)
        }
    }
}