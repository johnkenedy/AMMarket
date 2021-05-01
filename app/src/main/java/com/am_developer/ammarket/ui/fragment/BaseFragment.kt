package com.am_developer.ammarket.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.am_developer.ammarket.R
import com.google.android.material.snackbar.Snackbar

open class BaseFragment : Fragment() {

private lateinit var mProgressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    fun showSnackBarInFragment (message: String, errorMessage: Boolean) {
        val snackBar =
            activity?.let { Snackbar.make(it.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG) }
        val snackBarView = snackBar?.view

        if (errorMessage) {
            context?.let {
                ContextCompat.getColor(
                    it,
                    R.color.red
                )
            }?.let {
                snackBarView?.setBackgroundColor(
                    it
                )
            }
        } else {
            context?.let {
                ContextCompat.getColor(
                    it,
                    R.color.pink
                )
            }?.let {
                snackBarView?.setBackgroundColor(
                    it
                )
            }
        }
        snackBar?.show()
    }

    fun showProgressDialog() {
        mProgressDialog = context?.let { Dialog(it) }!!

        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()

    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

}