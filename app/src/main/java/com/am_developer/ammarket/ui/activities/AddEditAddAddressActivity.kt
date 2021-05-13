package com.am_developer.ammarket.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.am_developer.ammarket.databinding.ActivityAddEditAddAddressBinding

class AddEditAddAddressActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddEditAddAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}