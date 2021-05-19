package com.am_developer.ammarket.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.am_developer.ammarket.R
import com.am_developer.ammarket.databinding.ActivityCheckoutBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.*
import com.am_developer.ammarket.ui.adapter.CartItemsListAdapter
import com.am_developer.ammarket.utils.Constants

class CheckoutActivity : BaseActivity() {

    private var mAddressDetails: Address? = null
    private var mSubTotal: Double = 0.0
    private var mTotalAmount: Double = 0.0

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var mProductList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<CartItem>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)) {
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)
        }

        if (mAddressDetails != null) {
            binding.tvAddressType.text = mAddressDetails?.type
            binding.tvAddressFullName.text = mAddressDetails?.name
            binding.tvAddressDetails.text =
                "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            binding.tvAddressAdditionalNote.text = mAddressDetails?.additionalNote
        }

        binding.rgPaymentMode.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rb_cash_on_delivery) {
                binding.tilNeedChange.visibility = View.VISIBLE
            } else {
                binding.tilNeedChange.visibility = View.GONE
            }
        }
        getProductList()

        binding.btnCheckoutPlaceOrder.setOnClickListener {
            placeAnOrder()
        }

    }

    fun successProductsListFromFireStore(productList: ArrayList<Product>) {
        mProductList = productList
        getCartItemsList()
    }

    private fun getCartItemsList() {
        FirestoreClass().getCartList(this@CheckoutActivity)
    }

    fun orderPlacedSuccessfully() {
        hideProgressDialog()
        val intent = Intent(this@CheckoutActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun placeAnOrder() {
        showProgressDialog()

        if (mAddressDetails != null) {
            val order = Order (
                FirestoreClass().getCurrentUserID(),
                mCartItemsList,
                mAddressDetails!!,
                "My order: ${System.currentTimeMillis()}",
                mCartItemsList[0].image,
                mSubTotal.toString(),
                "10.0",
                mTotalAmount.toString()
                )

            FirestoreClass().placeOrder(this@CheckoutActivity, order)
        }


    }

    @SuppressLint("SetTextI18n")
    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()
        for (product in mProductList) {
            for (cartItem in cartList) {
                if (product.product_id == cartItem.product_id) {
                    cartItem.stock_quantity = product.stock_quantity
                }
            }
        }
        mCartItemsList = cartList

        binding.rvCheckoutCartListItems.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        binding.rvCheckoutCartListItems.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@CheckoutActivity, mCartItemsList, false)
        binding.rvCheckoutCartListItems.adapter = cartListAdapter

        for (item in mCartItemsList) {
            val availableQuantity = item.stock_quantity.toInt()
            if (availableQuantity > 0) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                mSubTotal += (price * quantity)
            }
        }

        binding.tvCheckoutSubTotal.text = "S$mSubTotal"
        binding.tvCheckoutShippingCharge.text = "$10.00"

        if (mSubTotal > 0) {
            binding.llCheckout.visibility = View.VISIBLE
            mTotalAmount = mSubTotal + 10.0
            binding.tvCheckoutTotalAmount.text = "$$mTotalAmount"
        } else {
            binding.llCheckout.visibility = View.GONE
        }
    }

    private fun getProductList() {
        showProgressDialog()
        FirestoreClass().getAllProductsList(this@CheckoutActivity)
    }

}

//when (mAddressDetails?.type) {
//    Constants.HOME -> {
//        binding.rbHome.isChecked = true
//    }
//    Constants.OFFICE -> {
//        binding.rbOffice.isChecked = true
//    }
//    else -> {
//        binding.rbOther.isChecked = true
//        binding.tilOtherDetails.visibility = View.VISIBLE
//        binding.etOtherDetails.setText(mAddressDetails?.otherDetails)
//    }
//}