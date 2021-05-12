package com.am_developer.ammarket.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.am_developer.ammarket.R
import com.am_developer.ammarket.models.CartItem
import com.am_developer.ammarket.utils.GlideLoader

open class CartItemsListAdapter(
    private val context: Context,
    private val list: ArrayList<CartItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_cart_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.findViewById(R.id.iv_item_cart_product_image))
            holder.itemView.findViewById<TextView>(R.id.tv_item_cart_product_title).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tv_item_cart_product_price).text = "$${model.price}"
            holder.itemView.findViewById<TextView>(R.id.tv_item_cart_product_quantity).text = model.cart_quantity
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}