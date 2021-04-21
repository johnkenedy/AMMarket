package com.am_developer.ammarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.am_developer.ammarket.databinding.ActivityMainBinding
import com.am_developer.ammarket.ui.cart.CartFragment
import com.am_developer.ammarket.ui.favorite.FavoriteFragment
import com.am_developer.ammarket.ui.home.HomeFragment
import com.am_developer.ammarket.ui.profile.ProfileFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var chipNavigationBar: ChipNavigationBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chipNavigationBar = binding.bottomNavMain
        chipNavigationBar.setItemSelected(R.id.navigation_home, true)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
            .commit()
        bottomMenu()
    }

    private fun bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener(object :
            ChipNavigationBar.OnItemSelectedListener {


            override fun onItemSelected(id: Int) {
                val fragment = when (id) {
                    R.id.navigation_home -> HomeFragment()
                    R.id.navigation_cart -> CartFragment()
                    R.id.navigation_wishlist -> FavoriteFragment()
                    R.id.navigation_profile -> ProfileFragment()
                    else -> HomeFragment()
                }

                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                    .commit()
            }
        })
    }
}
