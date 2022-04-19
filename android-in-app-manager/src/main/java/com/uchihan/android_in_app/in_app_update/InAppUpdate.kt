package com.uchihan.android_in_app.in_app_update

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.widget.Toast
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class InAppUpdate {

    private var mAppUpdateManager: AppUpdateManager? = null

    fun checkForUpdate(activity: Activity, updateType: Int, requestCode: Int) {
        var type = if (updateType == 0) AppUpdateType.FLEXIBLE else AppUpdateType.IMMEDIATE
        mAppUpdateManager = AppUpdateManagerFactory.create(activity)
        mAppUpdateManager!!.appUpdateInfo.addOnSuccessListener { result ->
            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && result.isUpdateTypeAllowed(type)
            ) {
                try {
                    mAppUpdateManager?.startUpdateFlowForResult(
                        result,
                        type,
                        activity,
                        requestCode
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

}