package com.purenative.system

import android.app.Activity
import android.content.Intent
import java.lang.ref.WeakReference

object IntentProxy {
    private var activityRef: WeakReference<Activity>? = null

    internal fun set(activity: Activity) {
        activityRef = WeakReference(activity)
    }

    fun startActivity(intent: Intent) {
        val activity = activityRef?.get() ?: return
        activity.startActivity(intent)
    }
}