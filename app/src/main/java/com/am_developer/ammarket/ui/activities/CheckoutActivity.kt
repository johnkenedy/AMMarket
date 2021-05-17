package com.am_developer.ammarket.ui.activities

import android.os.Bundle
import android.view.View
import com.am_developer.ammarket.R
import com.am_developer.ammarket.databinding.ActivityCheckoutBinding

class CheckoutActivity : BaseActivity() {

    private lateinit var binding : ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rgPaymentMode.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rb_cash_on_delivery) {
                binding.tilNeedChange.visibility = View.VISIBLE
            } else {
                binding.tilNeedChange.visibility = View.GONE
            }
        }
    }
}

//when (mAddressDetails?.type) {
//    Constants.HOME -> {
//        binding.rbHome.isChecked = true
//    }
//    Constants.OFFICE -> {
//        binding.rbOffice.isChecked = true
//    }
//    else -> {
//        binding.rbOther.isChecked = true
//        binding.tilOtherDetails.visibility = View.VISIBLE
//        binding.etOtherDetails.setText(mAddressDetails?.otherDetails)
//    }
//}