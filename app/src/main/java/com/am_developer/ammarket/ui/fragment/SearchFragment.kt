package com.am_developer.ammarket.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.am_developer.ammarket.databinding.FragmentSearchBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.Product
import com.am_developer.ammarket.ui.adapter.SearchItemListAdapter

class SearchFragment : BaseFragment() {

    private lateinit var binding : FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun successProductsListFromFireStore(productList: ArrayList<Product>) {
        hideProgressDialog()

        binding.rvSearchItems.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvSearchItems.setHasFixedSize(true)
        val adapterCategory = SearchItemListAdapter(requireActivity(), productList)
        binding.rvSearchItems.adapter = adapterCategory

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