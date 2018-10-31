package com.walenkamp.spotdeal

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.walenkamp.spotdeal.Entities.Customer
import kotlinx.android.synthetic.main.customer_list_item.view.*

class CustomerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // Sets the customer data on the CustomerItem
    fun bind(customer: Customer) {
        itemView.customer_name_tv.text = customer.name
        itemView.customer_address_tv.text = customer.address
        itemView.customer_email_tv.text = customer.email
        itemView.customer_phone_tv.text = customer.phone.toString()

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, CustomerActivity::class.java).putExtra(CUSTOMER_ID, customer)
            itemView.context.startActivity(intent)
        }
    }
}