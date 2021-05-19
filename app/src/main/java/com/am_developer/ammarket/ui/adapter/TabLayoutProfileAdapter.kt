package com.am_developer.ammarket.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.am_developer.ammarket.ui.fragment.LoginFragment
import com.am_developer.ammarket.ui.fragment.MyPurchasesFragment
import com.am_developer.ammarket.ui.fragment.UserDetailsFragment

class TabLayoutProfileAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    private var totalTabs: Int
) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                UserDetailsFragment()
            }
            1 -> {
                MyPurchasesFragment()
            }
            2 -> {
                LoginFragment()
            }
            else -> UserDetailsFragment()
        }
    }
}