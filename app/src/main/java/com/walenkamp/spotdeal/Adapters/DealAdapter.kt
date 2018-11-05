package com.walenkamp.spotdeal.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.R
import com.walenkamp.spotdeal.ViewHolders.DealHolder

class DealAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // Result list
    val results = mutableListOf<Deal>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(context).inflate(R.layout.deal_list_item, parent, false)
        return DealHolder(inflate)
    }

    // binds the view holder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is DealHolder) {
            holder.bind(results[position])
        }
    }

    // Gets the number of items
    override fun getItemCount(): Int {
        return results.size
    }

    // Sets the deals
    fun setDeals(deals: List<Deal>) {
        results.clear()
        results.addAll(deals)
        notifyDataSetChanged()
    }

}