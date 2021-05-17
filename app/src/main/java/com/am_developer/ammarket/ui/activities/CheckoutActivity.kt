package com.am_developer.ammarket.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.am_developer.ammarket.R
import com.am_developer.ammarket.databinding.ActivityCheckoutBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.Address
import com.am_developer.ammarket.models.CartItem
import com.am_developer.ammarket.models.Product
import com.am_developer.ammarket.utils.Constants

class CheckoutActivity : BaseActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var mProductList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<CartItem>
    private var mAddressDetails: Address? = null

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
    }

    fun successProductsListFromFireStore(productList: ArrayList<Product>) {
        mProductList = productList
        getCartItemsList()
    }

    private fun getCartItemsList() {
        FirestoreClass().getCartList(this@CheckoutActivity)
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()
        mCartItemsList = cartList
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