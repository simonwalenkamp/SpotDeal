package com.walenkamp.spotdeal.ViewHolders

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.walenkamp.spotdeal.Activities.CUSTOMER
import com.walenkamp.spotdeal.Activities.DEAL
import com.walenkamp.spotdeal.Activities.DealActivity
import com.walenkamp.spotdeal.Activities.SHOW_INVALID
import com.walenkamp.spotdeal.BLL.CustomerLogic
import com.walenkamp.spotdeal.BLL.DealLogic
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.Interface.ICallbackCustomer
import com.walenkamp.spotdeal.Interface.ICallbackDeal
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_order_history.view.*
import kotlinx.android.synthetic.main.history_list_item.view.*
import kotlinx.android.synthetic.main.order_list_item.view.*

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
            itemView.constraint_history_item.visibility = View.INVISIBLE
            itemView.progress_history_item.visibility = View.VISIBLE
            dealLogic.getDealById(order.dealId, object : ICallbackDeal{
                override fun onFinishDeal(deal: Deal?) {
                    customerLogic.getCustomerById(order.customerId, object : ICallbackCustomer{
                        override fun onFinishCustomer(customer: Customer?) {
                            val intent = Intent(itemView.context, DealActivity::class.java).putExtra(
                                DEAL, deal).putExtra(CUSTOMER, customer).putExtra(SHOW_INVALID, order.valid)
                            itemView.context.startActivity(intent)
                            itemView.progress_history_item.visibility = View.INVISIBLE
                        }
                    })
                }
            })
        }
    }
}