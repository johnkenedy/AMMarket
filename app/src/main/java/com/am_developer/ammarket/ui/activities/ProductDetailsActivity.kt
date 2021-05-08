package com.am_developer.ammarket.ui.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.am_developer.ammarket.databinding.ActivityProductDetailsBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.Product
import com.am_developer.ammarket.ui.adapter.RelatedItemListAdapter

class ProductDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun successRelatedProductsListFromFireStore(productList: ArrayList<Product>) {
        hideProgressDialog()
        binding.rvRelatedItem.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvRelatedItem.setHasFixedSize(true)
        val relatedAdapter = RelatedItemListAdapter(this, productList)
        binding.rvRelatedItem.adapter = relatedAdapter
    }

    private fun getRelatedProductListFromFireStore() {
        showProgressDialog()
        FirestoreClass().getProductsListToActivity(this)
    }

    override fun onResume() {
        super.onResume()
        getRelatedProductListFromFireStore()
    }

}