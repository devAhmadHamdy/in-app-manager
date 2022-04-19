package com.uchihan.android_in_app.in_app_review

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.OnCompleteListener

class InAppReview {

    fun startReview(activity: Activity,onReviewDone: () -> Unit){
        val manager = ReviewManagerFactory.create(activity)

        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener {
                    onReviewDone()
                }
            } else {
                Log.e("in-app-review", "task not successful")
            }
        }
    }
}