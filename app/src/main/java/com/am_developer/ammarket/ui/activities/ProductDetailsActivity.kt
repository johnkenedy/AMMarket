package com.am_developer.ammarket.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.am_developer.ammarket.R
import com.am_developer.ammarket.databinding.ActivityProductDetailsBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.CartItem
import com.am_developer.ammarket.models.Product
import com.am_developer.ammarket.ui.adapter.RelatedItemListAdapter
import com.am_developer.ammarket.utils.Constants
import com.am_developer.ammarket.utils.GlideLoader

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var mProductDetails: Product
    private var mProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        getProductDetails()

        binding.btnProductDetailsAddToCart.setOnClickListener(this)
        binding.btnProductDetailsGoToCart.setOnClickListener(this)

        setContentView(binding.root)
    }

    private fun getProductDetails() {
        showProgressDialog()
        FirestoreClass().getProductDetails(this@ProductDetailsActivity, mProductId)
        hideProgressDialog()
    }

    fun productExistsInCart() {
        hideProgressDialog()
        binding.btnProductDetailsAddToCart.visibility = View.INVISIBLE
        binding.btnProductDetailsGoToCart.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    fun productDetailsSuccess(product: Product) {
        mProductDetails = product
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            binding.ivProductDetailsImage
        )

        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsPrice.text = "$${product.price}"
//        binding.tvProductDetailsItemDescription.text = product.description

        if (product.stock_quantity.toInt() == 0) {
            hideProgressDialog()
            binding.btnProductDetailsAddToCart.visibility = View.GONE
            binding.tvProductDetailsPrice.text =
                resources.getString(R.string.lbl_out_of_stock)
        } else {
            FirestoreClass().checkIfItemExistInCart(this, mProductId)
        }
    }

    fun successRelatedProductsListFromFireStore(productList: ArrayList<Product>) {
        hideProgressDialog()
        binding.rvRelatedItem.layoutManager =
            LinearLayoutManager(this@ProductDetailsActivity)
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


    private fun addToCart() {
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialog()
        FirestoreClass().addCartItems(this, cartItem)
    }

    fun addToCartSuccess() {
        hideProgressDialog()
        showErrorSnackBar("Product was added to your cart.", false)

        binding.btnProductDetailsAddToCart.visibility = View.INVISIBLE
        binding.btnProductDetailsGoToCart.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_product_details_add_to_cart -> {
                    addToCart()
                }
                R.id.btn_product_details_go_to_cart -> {
                    val intent = Intent(this@ProductDetailsActivity, CartListActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        startActivity(Intent(this, MainActivity::class.java))
//    }
}