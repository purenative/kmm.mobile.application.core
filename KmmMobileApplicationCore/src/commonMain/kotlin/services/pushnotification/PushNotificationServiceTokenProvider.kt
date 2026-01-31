package com.purenative.services.pushnotification

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMPushNotificationServiceTokenProvider", "PushNotificationServiceTokenProvider", true)
interface PushNotificationServiceTokenProvider {
    var onTokenUpdated: ((String?) -> Unit)?
    fun retrieveDeviceToken()
}