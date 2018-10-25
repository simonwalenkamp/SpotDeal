package com.walenkamp.spotdeal

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.walenkamp.spotdeal.Entities.Customer
import kotlinx.android.synthetic.main.customer_list_item.view.*

class CustomerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // Sets the customer data on the CustomerItem
    fun bind(customer: Customer) {
        itemView.customer_name_tv.text = customer.name

        itemView.setOnClickListener {
            Log.d("SIMON", "${customer.name} Clicked!")
        }
    }
}