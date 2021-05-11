package com.am_developer.ammarket.ui.activities

import android.app.Dialog
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.am_developer.ammarket.R
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog

    private var doubleBackToExitPressedOnce = false

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.red
                )
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.pink
                )
            )
        }
        snackBar.show()
    }

    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        showErrorSnackBar("Please, click back again to exit.", false)
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    fun showProgressDialog() {
        mProgressDialog = Dialog(this)

        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()

    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun closeActivity() {
        finish()
    }
}