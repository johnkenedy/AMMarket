package com.am_developer.ammarket.activities

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.am_developer.ammarket.R
import com.am_developer.ammarket.databinding.ActivityMainBinding
import com.am_developer.ammarket.fragment.*
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var chipNavigationBar: ChipNavigationBar
    private lateinit var verticalChipNavigationBar: ChipNavigationBar

    private var upDownMenu = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chipNavigationBar = binding.bottomNavMain
        verticalChipNavigationBar = binding.verticalNavMain

        chipNavigationBar.setItemSelected(R.id.navigation_home, true)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
            .commit()

        val infinity = AnimationUtils.loadAnimation(this, R.anim.infinity)
        binding.infinity.animation = infinity

        binding.downUpMenu.setOnClickListener {
            btnMenuAnimation()
        }

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
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    fragment
                ).commit()

                verticalChipNavigationBar.setItemSelected(R.id.navigation_search, false)
                verticalChipNavigationBar.setItemSelected(R.id.navigation_cart, false)
                verticalChipNavigationBar.setItemSelected(R.id.navigation_coupon, false)
                verticalChipNavigationBar.setItemSelected(R.id.navigation_list, false)
                verticalChipNavigationBar.setItemSelected(R.id.navigation_address, false)
            }
        })
    }

    private fun verticalMenu() {
        verticalChipNavigationBar.setOnItemSelectedListener(object :
            ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                val fragment = when (id) {
                    R.id.navigation_search -> SearchFragment()
                    R.id.navigation_cart -> ShoppingFragment()
                    R.id.navigation_coupon -> CouponFragment()
                    R.id.navigation_list -> ShoppingListFragment()
                    R.id.navigation_store -> StoresFragment()
                    R.id.navigation_logout -> LogoutFragment()

                    else -> return
                }
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                    .commit()

                chipNavigationBar.setItemSelected(R.id.navigation_home, false)
                chipNavigationBar.setItemSelected(R.id.navigation_shopping, false)
                chipNavigationBar.setItemSelected(R.id.navigation_wishlist, false)
                chipNavigationBar.setItemSelected(R.id.navigation_profile, false)
            }
        })

    }

    private fun btnMenuAnimation() {
        if (upDownMenu == 1) {

            val downMenu = AnimationUtils.loadAnimation(this, R.anim.down_menu)
            binding.verticalNavMain.animation = downMenu
            binding.downUpMenu.setImageResource(R.drawable.arrow_up)
            binding.verticalNavMain.visibility = View.VISIBLE
            upDownMenu = 2
        } else {
            val upMenu = AnimationUtils.loadAnimation(this, R.anim.up_menu)
            binding.verticalNavMain.animation = upMenu
            binding.downUpMenu.setImageResource(R.drawable.arrow_down)
            upDownMenu = 1

        }
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }

}
