package com.walenkamp.spotdeal

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.walenkamp.spotdeal.Entities.Customer

class CustomerAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Result list
    val results = mutableListOf<Customer>()

    // Inflates the holder with CustomerItems
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(context).inflate(R.layout.customer_list_item, parent, false)
        return CustomerHolder(inflate)
    }

    // binds the view holder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is CustomerHolder) {
            holder.bind(results[position])
        }
    }

    // Gets the number of items
    override fun getItemCount(): Int {
        return results.size
    }

    // Sets the customers
    fun setCustomers(customers: List<Customer>) {
        results.addAll(customers)
    }

}