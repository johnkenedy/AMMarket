package com.am_developer.ammarket.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.am_developer.ammarket.R
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.CartItem
import com.am_developer.ammarket.ui.activities.CartListActivity
import com.am_developer.ammarket.utils.Constants
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

            if (model.cart_quantity == "0") {
                holder.itemView.findViewById<ImageView>(R.id.iv_item_cart_remove_item).visibility = View.INVISIBLE
                holder.itemView.findViewById<ImageView>(R.id.iv_item_cart_add_item).visibility = View.INVISIBLE
                holder.itemView.findViewById<TextView>(R.id.tv_item_cart_product_quantity).text =
                    context.resources.getString(R.string.lbl_out_of_stock)
            } else {
                holder.itemView.findViewById<ImageView>(R.id.iv_item_cart_remove_item).visibility = View.VISIBLE
                holder.itemView.findViewById<ImageView>(R.id.iv_item_cart_add_item).visibility = View.VISIBLE
            }

            holder.itemView.findViewById<ImageView>(R.id.iv_item_cart_delete_item).setOnClickListener {
                when(context){
                   is CartListActivity -> {
                        context.showProgressDialog()
                    }
                }
                FirestoreClass().removeItemFromCart(context, model.id)
            }

            holder.itemView.findViewById<ImageView>(R.id.iv_item_cart_remove_item).setOnClickListener {
                if (model.cart_quantity == "1") {
                    FirestoreClass().removeItemFromCart(context, model.id)
                } else {
                    val cartQuantity: Int = model.cart_quantity.toInt()
                    val itemHashMap = HashMap<String, Any>()

                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                    FirestoreClass().updateMyCart(context, model.id, itemHashMap)
                }
            }

            holder.itemView.findViewById<ImageView>(R.id.iv_item_cart_add_item).setOnClickListener {
                val cartQuantity: Int = model.cart_quantity.toInt()
                if (cartQuantity < model.stock_quantity.toInt()) {
                    val itemHashMap = HashMap<String, Any>()

                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                    FirestoreClass().updateMyCart(context, model.id, itemHashMap)
                } else {
                    if (context is CartListActivity) {
                        context.showErrorSnackBar(
                            context.resources.getString(
                                R.string.msg_for_available_stock, model.stock_quantity
                            ), true)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}