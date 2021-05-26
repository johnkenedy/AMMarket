package com.am_developer.ammarket.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.am_developer.ammarket.databinding.FragmentSearchBinding
import com.am_developer.ammarket.models.Product
import com.am_developer.ammarket.ui.adapter.SearchItemListAdapter
import com.am_developer.ammarket.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore

class SearchFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchBinding

    private val mFireStore = FirebaseFirestore.getInstance()

    private var searchList: List<Product> = ArrayList()
//    private val searchListAdapter =


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText: String = binding.etSearch.text.toString()
                searchInFireStore(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        return binding.root
    }

    fun searchInFireStore(searchText: String) {
        mFireStore.collection(Constants.PRODUCTS).orderBy("title")
            .startAt(searchText)
            .endAt("$searchText\uf8ff")
            .limit(20)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    searchList = it.result!!.toObjects(Product::class.java)
                    Log.e("Searched Items", searchList.toString())

                    binding.rvSearchItems.layoutManager =
                        LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                    binding.rvSearchItems.setHasFixedSize(true)
                    binding.rvSearchItems.adapter =  SearchItemListAdapter(requireActivity(), searchList as ArrayList<Product>)


                } else {
                    Log.e("Search Fragment", "Error while searching Products.")
                }
            }
    }

    override fun onResume() {
        super.onResume()
    }

}