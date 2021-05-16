package com.am_developer.ammarket.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.am_developer.ammarket.databinding.ActivityCartBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.CartItem
import com.am_developer.ammarket.models.Product
import com.am_developer.ammarket.ui.adapter.CartItemsListAdapter
import com.am_developer.ammarket.utils.Constants

class CartListActivity : BaseActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartListItems: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheckout.setOnClickListener {
            val intent = Intent(this@CartListActivity, AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }

    }

    @SuppressLint("SetTextI18n")
    fun successCartItemList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        for (product in mProductsList) {
            for (cartItem in cartList) {
                if (product.product_id == cartItem.product_id) {
                    cartItem.stock_quantity = product.quantity

                    if (product.quantity.toInt() == 0) {
                        cartItem.cart_quantity = product.quantity
                    }
                }
            }
        }

        mCartListItems = cartList

        if (mCartListItems.size > 0) {
            binding.rvCartItemsList.visibility = View.VISIBLE
            binding.llCheckout.visibility = View.VISIBLE
            binding.btnCheckout.visibility = View.VISIBLE
            binding.tvNoCartItemFound.visibility = View.GONE

            binding.rvCartItemsList.layoutManager = LinearLayoutManager(this@CartListActivity)
            binding.rvCartItemsList.setHasFixedSize(true)
            val cartListAdapter = CartItemsListAdapter(this@CartListActivity, cartList)
            binding.rvCartItemsList.adapter = cartListAdapter

            var subTotal: Double = 0.0
            for (item in mCartListItems) {
                val availableQuantity = item.stock_quantity.toInt()
                if (availableQuantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()
                    subTotal += (price * quantity)
                }
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

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
        mProductsList = productsList
        getCartItemsList()
        hideProgressDialog()
    }

    private fun getProductList() {
        showProgressDialog()
        FirestoreClass().getAllProductsList(this@CartListActivity)
    }

    fun itemUpdateSuccess() {
//        hideProgressDialog()
        getCartItemsList()
    }

    override fun onResume() {
        super.onResume()
        getProductList()
    }

    fun itemRemovedSuccess() {
        hideProgressDialog()
        showErrorSnackBar("Item removed successfully.", false)
        getCartItemsList()
    }

    private fun getCartItemsList() {
        FirestoreClass().getCartList(this@CartListActivity)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}