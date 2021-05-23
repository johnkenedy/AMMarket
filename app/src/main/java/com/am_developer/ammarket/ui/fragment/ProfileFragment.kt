package com.am_developer.ammarket.ui.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.am_developer.ammarket.R
import com.am_developer.ammarket.databinding.FragmentProfileBinding
import com.am_developer.ammarket.firestore.FirestoreClass
import com.am_developer.ammarket.models.User
import com.am_developer.ammarket.ui.activities.MainActivity
import com.am_developer.ammarket.ui.adapter.TabLayoutProfileAdapter
import com.am_developer.ammarket.utils.Constants
import com.am_developer.ammarket.utils.GlideLoader
import com.google.android.material.tabs.TabLayout
import java.io.IOException


class ProfileFragment : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.ivProfileUserImage.setOnClickListener(this@ProfileFragment)

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        tabLayout!!.addTab(tabLayout!!.newTab().setText("My Details"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("My Orders"))

        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        tabPosition()

        return binding.root
    }

    private fun tabPosition() {

        val adapter = activity?.let {
            TabLayoutProfileAdapter(
                requireActivity(),
                it.supportFragmentManager,
                tabLayout!!.tabCount
            )
        }

        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_profile_user_image -> {
//                if (
//                    context?.let {
//                        ContextCompat.checkSelfPermission(
//                            it, Manifest.permission.READ_EXTERNAL_STORAGE
//                        )
//                    } == PackageManager.PERMISSION_GRANTED) {
//                    Constants.showImageChooser(this@ProfileFragment)
//
//                } else {
//                    ActivityCompat.requestPermissions(
//                        context as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                        Constants.READ_STORAGE_PERMISSION_CODE
//                    )
//                }
            }
        }
    }


    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
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

    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }

    private fun getUserDetails() {
        showProgressDialog()
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User) {
        context?.let { GlideLoader(it).loadUserPicture(user.image, binding.ivProfileUserImage) }
        binding.tvFragmentProfileName.text = user.name
        binding.tvFragmentProfileEmail.text = user.email
        hideProgressDialog()
    }

    fun updatePhoto() {
        if (mSelectedImageFileUri != null) {
            FirestoreClass().uploadImageToCloudStorage(
                this@ProfileFragment,
                mSelectedImageFileUri
            )
        }
    }

    override fun onPause() {
        Log.i("detach test", "onPause called")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }
}