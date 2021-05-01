package com.am_developer.ammarket.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.am_developer.ammarket.R
import com.am_developer.ammarket.databinding.FragmentRegisterBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.User
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

            TextUtils.isEmpty(binding.etRegisterCpf.text.toString().trim { it <= ' ' }) -> {
                showSnackBarInFragment(
                    resources.getString(R.string.err_msg_enter_cpf),
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

                    if (it.isSuccessful) {
                        val firebaseUser: FirebaseUser = it.result!!.user!!

                        val user = User(
                            firebaseUser.uid,
                            binding.etRegisterName.text.toString().trim { it <= ' ' },
                            binding.etRegisterEmail.text.toString().trim { it <= ' ' },
                            binding.etRegisterCpf.text.toString().toLong()
                        )

                        FirestoreClass().registerUser(this@RegisterFragment, user)

                    } else {
                        hideProgressDialog()
                        showSnackBarInFragment(it.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userRegistrationSuccess() {
        hideProgressDialog()
        showSnackBarInFragment(resources.getString(R.string.register_success), false)
        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.login_fragment_container,
            LoginFragment()
        )?.commit()
    }

}
