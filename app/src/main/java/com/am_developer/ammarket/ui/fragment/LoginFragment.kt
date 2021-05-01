package com.am_developer.ammarket.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.am_developer.ammarket.R
import com.am_developer.ammarket.ui.activities.MainActivity
import com.am_developer.ammarket.databinding.FragmentLoginBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.User
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.btnLoginEnter.setOnClickListener {
            logInRegisteredUser()
        }

        return binding.root
    }

    fun userLoggedInSuccess(user: User) {
        hideProgressDialog()
        Log.i("First Name: ", user.name)
        startActivity(Intent(context, MainActivity::class.java))
        activity?.finish()
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etLoginEmail.text.toString().trim { it <= ' ' }) -> {
                showSnackBarInFragment(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding.etLoginPassword.text.toString().trim { it <= ' ' }) -> {
                showSnackBarInFragment(
                    resources.getString(R.string.err_msg_enter_password),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser() {

        if (validateLoginDetails()) {

            showProgressDialog()

            val email = binding.etLoginEmail.text.toString().trim { it <= ' ' }
            val password = binding.etLoginPassword.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        FirestoreClass().getUserDetails(this)

                    } else {
                        hideProgressDialog()
                        showSnackBarInFragment(task.exception!!.message.toString(), true)
                    }
                }
        }

    }
}