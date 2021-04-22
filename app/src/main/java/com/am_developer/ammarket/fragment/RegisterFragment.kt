package com.am_developer.ammarket.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.am_developer.ammarket.R
import com.am_developer.ammarket.activities.LoginActivity
import com.am_developer.ammarket.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterFragment : BaseFragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.btnRegisterRegister.setOnClickListener {
            registerUser()
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

            TextUtils.isEmpty(
                binding.etRegisterConfirmPassword.text.toString().trim { it <= ' ' }) -> {
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
                true
            }
        }
    }

    private fun registerUser() {
        if (validateRegisterDetails()) {

            showProgressDialog()

            val email: String = binding.etRegisterEmail.text.toString().trim { it <= ' ' }
            val password: String = binding.etRegisterPassword.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    OnCompleteListener<AuthResult> { task ->

                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!


                        } else {
                            showSnackBarInFragment(task.exception!!.message.toString(), true)
                        }
                    }
                    hideProgressDialog()
                    activity?.supportFragmentManager?.beginTransaction()?.replace(
                        R.id.login_fragment_container,
                        LoginFragment()
                    )?.commit()
                }
            LoginActivity().finish()
        }
    }
}
