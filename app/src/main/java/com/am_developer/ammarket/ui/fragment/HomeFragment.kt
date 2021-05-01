package com.am_developer.ammarket.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.am_developer.ammarket.databinding.FragmentHomeBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.Product
import com.am_developer.ammarket.ui.adapter.HomeProductsListAdapter

class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    fun successProductsListFromFireStore(productList: ArrayList<Product>) {
        hideProgressDialog()

        binding.rvHomeProducts.layoutManager = GridLayoutManager(activity, 2)
        binding.rvHomeProducts.setHasFixedSize(true)
        val adapterProducts = HomeProductsListAdapter(requireActivity(), productList)
        binding.rvHomeProducts.adapter = adapterProducts
    }

    private fun getProductListFromFireStore() {
        showProgressDialog()
        FirestoreClass().getProductsList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }

}