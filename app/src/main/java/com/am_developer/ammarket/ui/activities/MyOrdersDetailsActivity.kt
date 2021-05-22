package com.am_developer.ammarket.ui.activities

import android.os.Bundle
import com.am_developer.ammarket.databinding.ActivityMyOrdersDetailsBinding

class MyOrdersDetailsActivity : BaseActivity() {

    private lateinit var binding : ActivityMyOrdersDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyOrdersDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}