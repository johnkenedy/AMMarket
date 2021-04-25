package com.am_developer.ammarket.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.am_developer.ammarket.R
import com.am_developer.ammarket.databinding.FragmentProfileBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.User
import com.am_developer.ammarket.utils.Constants

class ProfileFragment : BaseFragment(), View.OnClickListener {

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
            context?.getSharedPreferences(Constants.LOGGED_IN_EMAIL, Context.MODE_PRIVATE)
        val email = emailSharedPreferences?.getString(Constants.LOGGED_IN_EMAIL, "")

        val cpfSharedPreferences =
            context?.getSharedPreferences(Constants.LOGGED_IN_CPF, Context.MODE_PRIVATE)
        val cpf = cpfSharedPreferences?.getString(Constants.LOGGED_IN_CPF, "")

        val mobileSharedPreferences =
            context?.getSharedPreferences(Constants.LOGGED_IN_MOBILE, Context.MODE_PRIVATE)
        val mobile = mobileSharedPreferences?.getString(Constants.LOGGED_IN_MOBILE, "")

        Log.i("PFName", "$name the email is $email")

        FirestoreClass().getUserDetails(this)

        binding.ivProfileChangeImageProfile.setOnClickListener(this@ProfileFragment)

        return root
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_profile_change_image_profile ->
                if (
                        context?.let {
                            ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE
                            ) } == PackageManager.PERMISSION_GRANTED) {
                        showSnackBarInFragment("You alrady have permission", false)
                      } else {
                        ActivityCompat.requestPermissions(
                            context as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constants.READ_STORAGE_PERMISSION_CODE)
                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSnackBarInFragment("The storage permission is granted.", false)
            } else {
                showSnackBarInFragment("The storage permission denied. You can also allow it from settings. ", true)

            }
        }
    }

    fun userInfo(user: User) {
        val name = user.name
        binding.etProfileName.isEnabled = false
        binding.etProfileName.setText(name)

        val email = user.email
        binding.etRegisterEmail.isEnabled = false
        binding.etRegisterEmail.setText(email)

        val cpf = user.cpf.toString()
        binding.etRegisterCpf.setText(cpf)

        val mobile = user.mobile.toString()
        binding.etRegisterPhoneNumber.setText(mobile)

        Log.i("xdre", "$name my name is ${user.name}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}