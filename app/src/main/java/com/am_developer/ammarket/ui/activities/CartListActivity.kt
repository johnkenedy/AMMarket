package com.am_developer.ammarket.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.am_developer.ammarket.databinding.ActivityCartBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.CartItem
import com.am_developer.ammarket.ui.adapter.CartItemsListAdapter

class CartListActivity : BaseActivity() {

    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    fun successCartItemList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        if (cartList.size > 0) {
            binding.rvCartItemsList.visibility = View.VISIBLE
            binding.llCheckout.visibility = View.VISIBLE
            binding.btnCheckout.visibility = View.VISIBLE
            binding.tvNoCartItemFound.visibility = View.GONE

            binding.rvCartItemsList.layoutManager = LinearLayoutManager(this@CartListActivity)
            binding.rvCartItemsList.setHasFixedSize(true)
            val cartListAdapter = CartItemsListAdapter(this@CartListActivity, cartList)
            binding.rvCartItemsList.adapter = cartListAdapter

            var subTotal: Double = 0.0
            for (item in cartList) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                subTotal += (price * quantity)
            }

            val roundedSubTotal = "%.2f".format(subTotal).toDouble()
            binding.tvSubTotal.text = "$$roundedSubTotal"
            binding.tvShippingCharge.text = "$10.0" //TODO - Change shipping charge Logic

            if (subTotal > 0) {
                binding.llCheckout.visibility = View.VISIBLE
                val total = subTotal + 10      //TODO - Change Logic Here
                val roundedTotal = "%.2f".format(total).toDouble()
                binding.tvTotalAmount.text = "$$roundedTotal"
            } else {
                binding.llCheckout.visibility = View.GONE
            }
        } else {
            binding.rvCartItemsList.visibility = View.GONE
            binding.llCheckout.visibility = View.GONE
            binding.btnCheckout.visibility = View.GONE
            binding.tvNoCartItemFound.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getCartItemsList()
    }

    private fun getCartItemsList() {
        showProgressDialog()
        FirestoreClass().getCartList(this@CartListActivity)
    }

}