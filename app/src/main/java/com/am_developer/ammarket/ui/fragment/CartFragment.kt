package com.am_developer.ammarket.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.am_developer.ammarket.databinding.FragmentCartBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.Product
import com.am_developer.ammarket.ui.adapter.HomeProductsListAdapter
import com.am_developer.ammarket.ui.adapter.TrendingProductsListAdapter

class CartFragment : BaseFragment() {

    private var _binding: FragmentCartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentCartBinding.inflate(inflater, container, false)

        return binding.root
    }

    fun successProductsListFromFireStore(productList: ArrayList<Product>) {
        hideProgressDialog()

        binding.rvShoppingTrendingFeatures.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val adapterCategory = HomeProductsListAdapter(requireActivity(), productList)
        binding.rvShoppingTrendingFeatures.adapter = adapterCategory

        binding.rvShoppingProductsFeatures.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvShoppingProductsFeatures.setHasFixedSize(true)
        val adapterProducts = TrendingProductsListAdapter(requireActivity(), productList)
        binding.rvShoppingProductsFeatures.adapter = adapterProducts
    }

    private fun getProductListFromFireStore() {
        showProgressDialog()
        FirestoreClass().getProductsList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}