package com.am_developer.ammarket.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.am_developer.ammarket.databinding.FragmentFavoritesBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.CartItem
import com.am_developer.ammarket.models.Product
import com.am_developer.ammarket.ui.adapter.FavoriteItemListAdapter
import com.am_developer.ammarket.utils.Constants
import com.am_developer.ammarket.utils.GlideLoader
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.related_item_holder.*

class FavoriteFragment : BaseFragment() {

    private var _binding: FragmentFavoritesBinding? = null

    private val mFireStore = FirebaseFirestore.getInstance()

    private var favoriteList: List<Product> = ArrayList()
    private lateinit var mProductDetails: Product
    private var mProductId: String = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getFavProductDetails() {
        showProgressDialog()
        FirestoreClass().getProductFavDetails(this@FavoriteFragment, mProductId)
        hideProgressDialog()
    }

    private fun favoriteItem() {
        mFireStore.collection(Constants.PRODUCTS).orderBy("favorite")
            .startAt("yes")
            .limit(20)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    favoriteList = it.result!!.toObjects(Product::class.java)
                    Log.e("Searched Items", favoriteList.toString())

                    binding.rvFavoriteItems.layoutManager =
                        LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                    binding.rvFavoriteItems.setHasFixedSize(true)

                    binding.rvFavoriteItems.adapter = FavoriteItemListAdapter(
                        requireActivity(),
                        favoriteList as ArrayList<Product>
                    )

                } else {
                    Log.e("Favorite Fragment", "Error while searching Products.")
                }
            }
    }

    @SuppressLint("SetTextI18n")
    fun getProductFavDetailsSuccess(product: Product) {
        mProductDetails = product
        GlideLoader(requireActivity()).loadProductPicture(
            product.image,
            iv_related_product_image
        )
        tv_related_product_title.text = product.title
        tv_related_product_price.text = "$${product.price}"
        tv_related_product_quantity.text = product.stock_quantity

        FirestoreClass().checkFromFragmentIfItemExistInCart(this, mProductId)
    }

    fun addFavToCartItems() {
        Log.e("cartItemFav", mProductDetails.toString())
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialog()
        FirestoreClass().addFavToCartItems(this, cartItem)
    }

    fun productExistsInCart() {
        hideProgressDialog()
    }

    fun addToCartSuccess() {
        hideProgressDialog()
        showSnackBarInFragment("Product was added to your cart.", false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        favoriteItem()
    }

}