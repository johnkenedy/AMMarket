package com.am_developer.ammarket.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.am_developer.ammarket.R
import com.am_developer.ammarket.activities.MainActivity
import com.am_developer.ammarket.databinding.FragmentProfileBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.User
import com.am_developer.ammarket.utils.Constants
import com.am_developer.ammarket.utils.GlideLoader
import java.io.IOException

class ProfileFragment : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.ivProfileChangeImageProfile.setOnClickListener(this@ProfileFragment)
        binding.btnProfileSaveChanges.setOnClickListener(this@ProfileFragment)

        return root
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_profile_change_image_profile ->
                if (
                    context?.let {
                        ContextCompat.checkSelfPermission(
                            it, Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    } == PackageManager.PERMISSION_GRANTED) {
                    Constants.showImageChooser(this@ProfileFragment)
                } else {
                    ActivityCompat.requestPermissions(
                        context as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constants.READ_STORAGE_PERMISSION_CODE
                    )
                }
            R.id.btn_profile_save_changes -> {

                if (validateUserProfileDetails()) {
                    showProgressDialog()

                    if (mSelectedImageFileUri != null)
                        FirestoreClass().uploadImageToCloudStorage(
                            this@ProfileFragment,
                            mSelectedImageFileUri
                        )
                } else {
                    updateUserProfileDetails()
                }
            }
        }
    }

    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        val mobileNumber =
            binding.etRegisterPhoneNumber.text.toString().trim { it <= ' ' }

        if (mobileNumber.isNotEmpty()) {
            userHashMap[Constants.LOGGED_IN_MOBILE] = mobileNumber
        }

        FirestoreClass().updateUserProfileData(this, userHashMap)
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()
        MainActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
            .commit()
        showSnackBarInFragment(
            "Profile updated successfully.",
            false
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@ProfileFragment)
            } else {
                showSnackBarInFragment(
                    "The storage permission denied. You can also allow it from settings. ",
                    true
                )

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        mSelectedImageFileUri = data.data
                        context?.let {
                            if (mSelectedImageFileUri != null) {
                                GlideLoader(it).loadUserPicture(
                                    mSelectedImageFileUri!!,
                                    binding.ivProfileUserImage
                                )
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        showSnackBarInFragment("Image selection Failed!", true)
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {

        }
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

    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }

    private fun getUserDetails() {
        showProgressDialog()
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User) {
        hideProgressDialog()
        context?.let { GlideLoader(it).loadUserPicture(user.image, binding.ivProfileUserImage) }
        binding.etProfileName.setText(user.name)
        binding.etProfileName.isEnabled = false
        binding.etRegisterCpf.setText(user.cpf.toString())
        binding.etRegisterCpf.isEnabled = false
        binding.etRegisterEmail.setText(user.email)
        binding.etRegisterEmail.isEnabled = false
        binding.etRegisterPhoneNumber.setText(user.mobile)
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}