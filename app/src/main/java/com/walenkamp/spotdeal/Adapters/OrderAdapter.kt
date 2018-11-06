package com.walenkamp.spotdeal.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.walenkamp.spotdeal.Entities.Order
import com.walenkamp.spotdeal.R
import com.walenkamp.spotdeal.ViewHolders.OrderHolder



class OrderAdapter(private val context: Context, val clickListener: (Order) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // Result list
    val results = mutableListOf<Order>()

    // Inflates the holder with OrderItems
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(context).inflate(R.layout.order_list_item, parent, false)
        return OrderHolder(inflate)
    }

    // binds the view holder and adds the clickListener
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is OrderHolder) {
            holder.bind(results[position], clickListener)
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    // Gets the number of items
    override fun getItemCount(): Int {
        return results.size
    }

    // Sets the orders
    fun setOrders(orders: List<Order>) {
        results.clear()
        results.addAll(orders)
        notifyDataSetChanged()
    }
}