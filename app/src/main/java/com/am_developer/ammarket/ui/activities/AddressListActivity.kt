package com.am_developer.ammarket.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.am_developer.ammarket.R
import com.am_developer.ammarket.databinding.ActivityAddressListBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.Address
import com.am_developer.ammarket.ui.adapter.AddressListAdapter
import com.am_developer.ammarket.utils.Constants
import com.am_developer.ammarket.utils.SwipeToDeleteCallback
import com.am_developer.ammarket.utils.SwipeToEditCallback

class AddressListActivity : BaseActivity() {

    private lateinit var binding: ActivityAddressListBinding
    private var mSelectAddress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddAddress.setOnClickListener {
           val intent =  Intent(this@AddressListActivity, AddEditAddAddressActivity::class.java)
            startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)

        }

        getAddressList()

        if (intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)) {
            mSelectAddress = intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS, false)
        }

        if (mSelectAddress) {
            binding.tvActivityAddressTitle.text = resources.getString(R.string.title_select_address)
        }

    }

    private fun getAddressList() {
        showProgressDialog()
        FirestoreClass().getAddressesList(this)
    }

    fun successAddressListFromFireStore(addressList: ArrayList<Address>) {
        hideProgressDialog()
        if (addressList.size > 0) {
            binding.rvAddressList.visibility = View.VISIBLE
            binding.tvNoAddressFound.visibility = View.GONE
            binding.rvAddressList.layoutManager = LinearLayoutManager(this@AddressListActivity)
            binding.rvAddressList.setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this, addressList, mSelectAddress)
            binding.rvAddressList.adapter = addressAdapter

            if (!mSelectAddress) {

                val editSwipeHandler = object : SwipeToEditCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = binding.rvAddressList.adapter as AddressListAdapter
                        adapter.notifyEditItem(this@AddressListActivity, viewHolder.adapterPosition)
                    }
                }

                val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(binding.rvAddressList)

                val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        showProgressDialog()
                        FirestoreClass().deleteAddress(
                            this@AddressListActivity,
                            addressList[viewHolder.adapterPosition].id
                        )
                    }
                }

                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(binding.rvAddressList)
            }

        } else {
            binding.rvAddressList.visibility = View.GONE
            binding.tvNoAddressFound.visibility = View.VISIBLE
        }
    }

    fun deleteAddressSuccess() {
        hideProgressDialog()
        showErrorSnackBar(
            resources.getString(R.string.err_your_address_deleted_successfully),
            false
        )
        getAddressList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            getAddressList()
        }
    }

//    override fun onResume() {
//        super.onResume()
//        getAddressList()
//    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}