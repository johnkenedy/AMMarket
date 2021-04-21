package com.am_developer.ammarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.am_developer.ammarket.databinding.ActivityMainBinding
import com.am_developer.ammarket.fragment.*
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var chipNavigationBar: ChipNavigationBar
    private lateinit var verticalChipNavigationBar: ChipNavigationBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chipNavigationBar = binding.bottomNavMain
        verticalChipNavigationBar = binding.verticalNavMain

        chipNavigationBar.setItemSelected(R.id.navigation_home, true)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
            .commit()

        bottomMenu()
        verticalMenu()
    }

    private fun bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener(object :
            ChipNavigationBar.OnItemSelectedListener {


            override fun onItemSelected(id: Int) {
                val fragment = when (id) {
                    R.id.navigation_home -> HomeFragment()
                    R.id.navigation_shopping -> CartFragment()
                    R.id.navigation_wishlist -> FavoriteFragment()
                    R.id.navigation_profile -> ProfileFragment()
                    else -> HomeFragment()
                }

                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                    .commit()
            }
        })
    }

    private fun verticalMenu() {

        verticalChipNavigationBar.setOnItemSelectedListener(object :
        ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                val fragment = when (id) {
                    R.id.navigation_cart -> ShoppingFragment()
                    R.id.navigation_coupon -> CouponFragment()
                    R.id.navigation_list -> ShoppingListFragment()
                    R.id.navigation_store -> StoresFragment()

                    else -> return
                }

                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                    .commit()
            }
        })
    }
}
