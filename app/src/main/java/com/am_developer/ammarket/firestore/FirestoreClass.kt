package com.am_developer.ammarket.firestore

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.am_developer.ammarket.models.Product
import com.am_developer.ammarket.models.User
import com.am_developer.ammarket.ui.activities.ProductDetailsActivity
import com.am_developer.ammarket.ui.fragment.*
import com.am_developer.ammarket.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(fragment: RegisterFragment, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                fragment.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                fragment.hideProgressDialog()
                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    @SuppressLint("CommitPrefEdits")
    fun getUserDetails(fragment: Fragment) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(fragment.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    fragment.context?.getSharedPreferences(
                        Constants.AM_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    user.name
                )
                editor.apply()

                when (fragment) {
                    is LoginFragment -> {
                        fragment.userLoggedInSuccess(user)
                    }
                    is ProfileFragment -> {
                        fragment.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (fragment) {
                    is LoginFragment -> {
                        fragment.hideProgressDialog()
                    }
                    is ProfileFragment -> {
                        fragment.hideProgressDialog()
                    }
                }
                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )

            }
    }

    fun updateUserProfileData(fragment: Fragment, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {

                when (fragment) {
                    is ProfileFragment -> {
                        fragment.userProfileUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener { e ->
                when (fragment) {
                    is ProfileFragment -> {
                        fragment.hideProgressDialog()
                    }
                }

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while updating the user details.", e
                )

            }
    }

    fun uploadImageToCloudStorage(fragment: Fragment, imageFileURI: Uri?) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "." +
                    Constants.getFileExtension(fragment, imageFileURI)
        )

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    when (fragment) {
                        is ProfileFragment -> {
                            fragment.imageUploadSuccess(uri.toString())
                        }
                    }
                }
        }
            .addOnFailureListener { exception ->
                when (fragment) {
                    is ProfileFragment -> {
                        fragment.hideProgressDialog()
                    }
                }

                Log.e(
                    fragment.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun getProductsList(fragment: Fragment) {
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }

                when (fragment) {
                    is HomeFragment ->
                        fragment.successProductsListFromFireStore(productsList)
                    is CartFragment ->
                        fragment.successProductsListFromFireStore(productsList)

                }

            }
    }

    fun getProductsListToActivity(activity: Activity) {
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }

                when (activity) {
                    is ProductDetailsActivity ->
                        activity.successRelatedProductsListFromFireStore(productsList)

                }

            }
    }

}