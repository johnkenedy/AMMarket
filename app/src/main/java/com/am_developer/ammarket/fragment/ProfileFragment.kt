package com.am_developer.ammarket.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.am_developer.ammarket.databinding.FragmentProfileBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.User
import com.am_developer.ammarket.utils.Constants

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val usernameSharedPreferences =
            context?.getSharedPreferences(Constants.LOGGED_IN_USERNAME, Context.MODE_PRIVATE)
        val name = usernameSharedPreferences?.getString(Constants.LOGGED_IN_USERNAME, "")

        val emailSharedPreferences =
            context?.getSharedPreferences( Constants.LOGGED_IN_EMAIL, Context.MODE_PRIVATE)
        val email = emailSharedPreferences?.getString(Constants.LOGGED_IN_EMAIL, "")
        Log.i("xdra", "$name the email is $email")

       FirestoreClass().getUserDetails(this)

        return root
    }

    fun userInfo(user: User) {
        val name = user.name
        binding.etProfileName.isEnabled = false
        binding.etProfileName.setText(name)

        val email = user.email
        binding.etRegisterEmail.isEnabled = false
        binding.etRegisterEmail.setText(email)

        Log.i("xdre", "$name my name is ${user.name}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}