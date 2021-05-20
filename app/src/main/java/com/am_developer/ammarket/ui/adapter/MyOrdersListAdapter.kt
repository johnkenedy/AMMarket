package com.am_developer.ammarket.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.am_developer.ammarket.R
import com.am_developer.ammarket.models.Order
import com.am_developer.ammarket.utils.GlideLoader
import kotlinx.android.synthetic.main.item_order_list_layout.view.*

class MyOrdersListAdapter(
    private val context: Context,
    private var list: ArrayList<Order>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_order_list_layout, parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(
                model.image,
                holder.itemView.iv_order_item_image
            )

            holder.itemView.tv_order_item_title.text = model.title
            holder.itemView.tv_order_item_address.text = "${model.address.address}, ${model.address.zipCode}"
            holder.itemView.tv_order_item_payment_mode.text = model.payment_mode
            holder.itemView.tv_order_item_price.text = "Total: $${model.total_amount}"
            holder.itemView.tv_order_item_shipping_progress.text = model.shipping_progress
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}