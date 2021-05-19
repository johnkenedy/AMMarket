package com.am_developer.ammarket.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.am_developer.ammarket.R
import com.am_developer.ammarket.databinding.FragmentUserDetailsBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.User
import com.am_developer.ammarket.ui.activities.MainActivity
import com.am_developer.ammarket.utils.Constants

class UserDetailsFragment : BaseFragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }


    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()

        val mobileNumber =
            binding.etRegisterPhoneNumber.text.toString().trim { it <= ' ' }

        if (mobileNumber.isNotEmpty()) {
            userHashMap[Constants.LOGGED_IN_MOBILE] = mobileNumber
        }

        FirestoreClass().updateUserProfileData(this, userHashMap)
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()
        startActivity(Intent(activity, MainActivity::class.java))
        showSnackBarInFragment(
            "Profile updated successfully.",
            false
        )
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etRegisterPhoneNumber.text.toString().trim { it <= ' ' }) -> {
                showSnackBarInFragment(
                    resources.getString(R.string.err_msg_enter_mobile_number),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }

    fun userDetailsSuccess(user: User) {
        hideProgressDialog()
        binding.etProfileName.setText(user.name)
        binding.etProfileName.isEnabled = false
        binding.etRegisterCpf.setText(user.cpf.toString())
        binding.etRegisterCpf.isEnabled = false
        binding.etRegisterEmail.setText(user.email)
        binding.etRegisterEmail.isEnabled = false
        binding.etRegisterPhoneNumber.setText(user.mobile)
        binding.etRegisterPhoneNumber.isEnabled = false
    }

    private fun getUserDetails() {
        showProgressDialog()
        FirestoreClass().getUserDetails(this)
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

}