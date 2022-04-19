package com.uchihan.androidinapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.uchihan.android_in_app.in_app_billing.InAppBilling
import com.uchihan.android_in_app.in_app_review.InAppReview
import com.uchihan.android_in_app.in_app_update.InAppUpdate

class MainActivity : AppCompatActivity() {
    private val UPDATE_REQUEST_CODE = 101
    private val UPDATE_TYPE_FLEXIBLE = 0
    private val UPDATE_TYPE_IMMEDIATE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // calling in app review
        InAppReview().startReview(
            this
        ) {
            Log.d("in app review", "success")
        }

        // calling in app update
        InAppUpdate().checkForUpdate(this, UPDATE_TYPE_FLEXIBLE, UPDATE_REQUEST_CODE)

        // calling in app billing
        InAppBilling().startPurchase(this, "YOUR_PACKAGE_ID") {
            Log.e(
                "Billing result",
                "Done"
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE) {
            Log.d("in app update", "app updated")
        }
    }


}