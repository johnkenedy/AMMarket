package com.am_developer.ammarket.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.am_developer.ammarket.R
import com.am_developer.ammarket.models.Product
import com.am_developer.ammarket.ui.activities.ProductDetailsActivity
import com.am_developer.ammarket.utils.Constants
import com.am_developer.ammarket.utils.GlideLoader

class RelatedItemListAdapter (
    private val context: Context,
    private val list: ArrayList<Product>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return MyViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.related_item_holder, parent, false)
            )
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val model = list[position]

            if (holder is MyViewHolder) {
                GlideLoader(context).loadProductPicture(
                    model.image,
                    holder.itemView.findViewById(R.id.iv_related_product_image)
                )
                holder.itemView.findViewById<TextView>(R.id.tv_related_product_title).text =
                    model.title
                holder.itemView.findViewById<TextView>(R.id.tv_related_product_price).text =
                    "$${model.price}"
                holder.itemView.findViewById<TextView>(R.id.tv_related_product_quantity).text =
                    model.quantity

                holder.itemView.setOnClickListener {
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                    context.startActivity(intent)
                }
            }
        }

        override fun getItemCount(): Int = list.size

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }