package com.am_developer.ammarket.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.am_developer.ammarket.R
import com.am_developer.ammarket.models.Product
import com.am_developer.ammarket.utils.GlideLoader

class RelatedItemListAdapter(
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
                model.stock_quantity

            if (model.favorite == "yes") {
                holder.itemView.findViewById<ImageView>(R.id.add_related_item_to_fav)
                    .setImageResource(R.drawable.ic_heart_full)
            } else {
                model.favorite = "no"
                holder.itemView.findViewById<ImageView>(R.id.add_related_item_to_fav)
                    .setImageResource(R.drawable.heart)
            }

            holder.itemView.findViewById<ImageView>(R.id.add_related_item_to_fav)
                .setOnClickListener {
                    if (model.favorite == "yes") {
                        holder.itemView.findViewById<ImageView>(R.id.add_related_item_to_fav)
                            .setImageResource(R.drawable.heart)
                        Toast.makeText(context, "Product removed to Fav", Toast.LENGTH_SHORT).show()
                    } else {
                        model.favorite = "yes"
                        holder.itemView.findViewById<ImageView>(R.id.add_related_item_to_fav)
                            .setImageResource(R.drawable.ic_heart_full)
                        Toast.makeText(context, "Product added to Fav", Toast.LENGTH_SHORT).show()
                    }
                }


//            holder.itemView.setOnClickListener {
//                val intent = Intent(context, ProductDetailsActivity::class.java)
//                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                context.startActivity(intent)
//                BaseActivity().closeActivity()
//            }
        }
    }


    override fun getItemCount(): Int = list.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}