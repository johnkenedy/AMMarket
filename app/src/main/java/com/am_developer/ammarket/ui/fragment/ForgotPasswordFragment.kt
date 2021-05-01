package com.am_developer.ammarket.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.am_developer.ammarket.R
import com.am_developer.ammarket.databinding.FragmentForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordFragment : BaseFragment() {

    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

        binding.btnRecoverPassword.setOnClickListener {
            recoverPassword()
        }

        return binding.root
    }

    private fun recoverPassword() {
        val email: String = binding.etRecoverEmail.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            showSnackBarInFragment(resources.getString(R.string.err_msg_enter_email), true)
        } else {
            showProgressDialog()
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        showSnackBarInFragment(
                            resources.getString(R.string.recover_successfully_msg),
                            false
                        )
                        activity?.supportFragmentManager?.beginTransaction()?.replace(
                            R.id.login_fragment_container,
                            LoginFragment()
                        )?.commit()
                    } else {
                        showSnackBarInFragment(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

}