package com.am_developer.ammarket.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.am_developer.ammarket.databinding.ActivityAddressListBinding

class AddressListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddAddress.setOnClickListener {
            startActivity(Intent(this, AddEditAddAddressActivity::class.java))
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}