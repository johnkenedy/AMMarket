package com.am_developer.ammarket.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.am_developer.ammarket.databinding.FragmentMyPurchasesBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.Order
import com.am_developer.ammarket.ui.adapter.MyOrdersListAdapter

class MyPurchasesFragment : BaseFragment() {

    private lateinit var binding: FragmentMyPurchasesBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyPurchasesBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun populateOrdersListUnUI(ordersList: ArrayList<Order>) {
        hideProgressDialog()

        if (ordersList.size > 0) {
            binding.rvMyOrderItems.visibility = View.VISIBLE
            binding.tvNoOrdersFound.visibility = View.GONE

            binding.rvMyOrderItems.layoutManager = LinearLayoutManager(activity)
            binding.rvMyOrderItems.setHasFixedSize(true)

            val myOrdersAdapter = MyOrdersListAdapter(requireActivity(), ordersList)
            binding.rvMyOrderItems.adapter = myOrdersAdapter

        } else {
            binding.rvMyOrderItems.visibility = View.GONE
            binding.tvNoOrdersFound.visibility = View.VISIBLE
        }
    }

    private fun getMyOrdersList() {
        showProgressDialog()
        FirestoreClass().getMyOrdersList(this@MyPurchasesFragment)
    }

    override fun onResume() {
        super.onResume()
        getMyOrdersList()
    }

}