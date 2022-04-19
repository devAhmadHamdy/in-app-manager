package com.uchihan.android_in_app.in_app_billing

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class InAppBilling  {
    private lateinit var action: () -> Unit
    private lateinit var billingClient: BillingClient

    open fun startPurchase(activity: Activity, productId: String, onReviewDone: () -> Unit) {
        action = onReviewDone
        billingClient =
            BillingClient.newBuilder(activity).enablePendingPurchases().setListener(purchaseListener).build();
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    GlobalScope.launch {
                        val flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(querySkuDetails(productId)[0])
                            .build()
                        billingClient.launchBillingFlow(
                            activity,
                            flowParams
                        ).responseCode
                    }


                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })

    }

    private suspend fun querySkuDetails(productId: String): List<SkuDetails> {
        val skuList = ArrayList<String>()
        skuList.add(productId)

        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

        // leverage querySkuDetails Kotlin extension function
        val skuDetailsResult = withContext(Dispatchers.IO) {
            billingClient.querySkuDetails(params.build())
        }

        // Process the result.
        return skuDetailsResult.skuDetailsList!!
    }

    private var purchaseListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    if (!purchase.isAcknowledged) {
                        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.purchaseToken)
                        GlobalScope.launch {
                            withContext(Dispatchers.IO) {
                                billingClient.acknowledgePurchase(acknowledgePurchaseParams.build()) { billingResult ->
                                    when (billingResult.responseCode) {
                                        BillingClient.BillingResponseCode.OK -> {
                                            Log.e(
                                                "Billing result",
                                                "OK"
                                            )
                                            // add action
                                            action()
                                        }
                                        else -> {
                                            Log.e(
                                                "BillingClient",
                                                "Failed to acknowledge purchase $billingResult"
                                            )
                                        }
                                    }
                                }
                            }


                        }
                    } else {
                        Log.e(
                            "isAcknowledged",
                            "true"
                        )
                        // add action
                        action()
                    }

                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                Log.e("purchase", "cancelled")
            } else {
                Log.e("purchase", "other error")
            }
        }

}