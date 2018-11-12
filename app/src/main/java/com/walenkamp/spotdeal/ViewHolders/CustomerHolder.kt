package com.walenkamp.spotdeal.ViewHolders

import android.content.Intent
import android.content.res.Resources
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.walenkamp.spotdeal.Activities.CUSTOMER
import com.walenkamp.spotdeal.Activities.CustomerActivity
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.R
import io.grpc.internal.SharedResourceHolder
import kotlinx.android.synthetic.main.customer_list_item.view.*

class CustomerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // Sets the customer data on the CustomerItem
    fun bind(customer: Customer) {

        itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.rec_background)

        itemView.customer_name_tv.text = customer.name
        itemView.customer_address_tv.text = customer.address
        itemView.customer_email_tv.text = customer.email
        itemView.customer_phone_tv.text = customer.phone.toString()

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, CustomerActivity::class.java).putExtra(
                CUSTOMER, customer)
            itemView.context.startActivity(intent)
        }
    }
}