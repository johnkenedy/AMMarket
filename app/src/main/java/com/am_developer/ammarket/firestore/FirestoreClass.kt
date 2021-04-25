package com.am_developer.ammarket.firestore

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment
import com.am_developer.ammarket.fragment.LoginFragment
import com.am_developer.ammarket.fragment.ProfileFragment
import com.am_developer.ammarket.fragment.RegisterFragment
import com.am_developer.ammarket.models.User
import com.am_developer.ammarket.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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
                editor.putString(
                    Constants.LOGGED_IN_EMAIL,
                    user.email
                )
                editor.apply()

                when (fragment) {
                    is LoginFragment -> {
                        fragment.userLoggedInSuccess(user)
                    }
                    is ProfileFragment -> {
                        fragment.userInfo(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (fragment) {
                    is LoginFragment -> {
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

}