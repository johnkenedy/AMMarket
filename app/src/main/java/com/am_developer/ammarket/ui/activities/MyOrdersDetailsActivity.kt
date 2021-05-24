package com.am_developer.ammarket.ui.activities

import android.os.Bundle
import com.am_developer.ammarket.databinding.ActivityMyOrdersDetailsBinding
import com.am_developer.ammarket.models.Order
import com.am_developer.ammarket.utils.Constants

class MyOrdersDetailsActivity : BaseActivity() {

    private lateinit var binding : ActivityMyOrdersDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyOrdersDetailsBinding.inflate(layoutInflater)

        var myOrderDetails: Order
        if (intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)) {
            myOrderDetails = intent.getParcelableExtra<Order>(Constants.EXTRA_MY_ORDER_DETAILS)!!
        }

        setContentView(binding.root)
    }
}