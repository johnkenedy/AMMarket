package com.am_developer.ammarket.ui.activities

import android.os.Bundle
import com.am_developer.ammarket.R
import com.am_developer.ammarket.databinding.ActivityLoginBinding
import com.am_developer.ammarket.ui.fragment.ForgotPasswordFragment
import com.am_developer.ammarket.ui.fragment.LoginFragment
import com.am_developer.ammarket.ui.fragment.RegisterFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var chipNavigationBar: ChipNavigationBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chipNavigationBar = binding.bottomNavRegisterLogin

        chipNavigationBar.setItemSelected(R.id.navigation_login, true)

        supportFragmentManager.beginTransaction().replace(
            R.id.login_fragment_container,
            LoginFragment()
        ).commit()

        bottomLoginMenu()

    }

    private fun bottomLoginMenu() {
        chipNavigationBar.setOnItemSelectedListener(object :
            ChipNavigationBar.OnItemSelectedListener {

            override fun onItemSelected(id: Int) {
                val fragment = when (id) {
                    R.id.navigation_login -> LoginFragment()
                    R.id.navigation_register -> RegisterFragment()
                    R.id.navigation_forgot_pasword -> ForgotPasswordFragment()
                    else -> return
                }

                supportFragmentManager.beginTransaction().replace(
                    R.id.login_fragment_container,
                    fragment
                ).commit()
            }
        })
    }


}