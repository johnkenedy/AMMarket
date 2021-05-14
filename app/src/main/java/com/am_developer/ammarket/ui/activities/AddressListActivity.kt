package com.am_developer.ammarket.ui.activities

import android.content.Intent
import android.os.Bundle
import com.am_developer.ammarket.databinding.ActivityAddressListBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.Address

class AddressListActivity : BaseActivity() {

    private lateinit var binding: ActivityAddressListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddAddress.setOnClickListener {
            startActivity(Intent(this, AddEditAddAddressActivity::class.java))
        }

        getAddressList()
    }

    private fun getAddressList() {
        showProgressDialog()
        FirestoreClass().getAddressesList(this)
    }

    fun successAddressListFromFireStore(addressList: ArrayList<Address>) {
        hideProgressDialog()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}