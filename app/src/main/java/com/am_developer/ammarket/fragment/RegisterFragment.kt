package com.am_developer.ammarket.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.am_developer.ammarket.R
import com.am_developer.ammarket.databinding.FragmentRegisterBinding

class RegisterFragment : BaseFragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.btnRegisterRegister.setOnClickListener {
            validateRegisterDetails()
        }

        return binding.root
    }

    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etRegisterName.text.toString().trim { it <= ' ' }) -> {
                showSnackBarInFragment(
                    resources.getString(R.string.err_msg_enter_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.etRegisterEmail.text.toString().trim { it <= ' ' }) -> {
                showSnackBarInFragment(
                    resources.getString(R.string.err_msg_enter_email),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.etRegisterPassword.text.toString().trim { it <= ' ' }) -> {
                showSnackBarInFragment(
                    resources.getString(R.string.err_msg_enter_password),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.etRegisterConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                showSnackBarInFragment(
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.etRegisterName.text.toString().trim { it <= ' ' }) -> {
                showSnackBarInFragment(
                    resources.getString(R.string.err_msg_enter_name),
                    true
                )
                false
            }

            binding.etRegisterPassword.text.toString().trim { it <= ' ' }
                    != binding.etRegisterConfirmPassword.text.toString().trim { it <= ' ' } -> {
                showSnackBarInFragment(
                    resources.getString(
                        R.string.err_msg_password_and_confirm_password_mismatch
                    ), true
                )
                false
            }

            else -> {
                showSnackBarInFragment("Your details are valid.", false)
                true
            }
        }
    }


}
