package com.am_developer.ammarket.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.am_developer.ammarket.databinding.ActivityProductDetailsBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.Product
import com.am_developer.ammarket.ui.adapter.RelatedItemListAdapter
import com.am_developer.ammarket.utils.Constants
import com.am_developer.ammarket.utils.GlideLoader

class ProductDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private var mProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        getProductDetails()

        setContentView(binding.root)
    }

    private fun getProductDetails() {
        showProgressDialog()
        FirestoreClass().getProductDetails(this@ProductDetailsActivity, mProductId)
        hideProgressDialog()
    }

    fun productDetailsSuccess(product: Product) {
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            binding.ivProductDetailsImage
        )

        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsPrice.text = "$${product.price}"
//        binding.tvProductDetailsItemDescription.text = product.description
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}