package com.am_developer.ammarket.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.am_developer.ammarket.databinding.FragmentCartBinding

class CartFragment : Fragment() {

private var _binding: FragmentCartBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {


    _binding = FragmentCartBinding.inflate(inflater, container, false)
    val root: View = binding.root

    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}