package com.am_developer.ammarket.ui.activities

import android.os.Bundle
import com.am_developer.ammarket.databinding.ActivityCartBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.CartItem

class CartListActivity : BaseActivity() {

    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun successCartItemList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()
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