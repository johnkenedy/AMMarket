package com.am_developer.ammarket.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.am_developer.ammarket.databinding.FragmentClientCartBinding
import com.am_developer.ammarket.ui.activities.CartActivity

class ClientCartFragment : BaseFragment() {

    private lateinit var binding: FragmentClientCartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentClientCartBinding.inflate(inflater, container, false)
        startActivity(Intent(requireActivity(), CartActivity::class.java))
        return binding.root
    }



}