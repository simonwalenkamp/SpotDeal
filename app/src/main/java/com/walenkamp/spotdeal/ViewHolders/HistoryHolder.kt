package com.walenkamp.spotdeal.ViewHolders

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.walenkamp.spotdeal.Activities.*
import com.walenkamp.spotdeal.BLL.CustomerLogic
import com.walenkamp.spotdeal.BLL.DealLogic
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackCustomer
import com.walenkamp.spotdeal.Interface.ICallbackDeal
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.history_list_item.view.*

class HistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // DealLogic instance
    private val dealLogic: DealLogic = DealLogic()

    // CustomerLogic instance
    private val customerLogic: CustomerLogic = CustomerLogic()

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

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, DealActivity::class.java).putExtra(
                DEAL_ID, order.dealId).putExtra(CUSTOMER_ID, order.customerId).putExtra(SHOW_INVALID, order.valid)
            itemView.context.startActivity(intent)
        }
    }
}