package com.purenative.services.usersession

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMUserSessionObserver", "UserSessionObserver", true)
interface UserSessionObserver {
    val userSessionObserverIdentifier: String

    fun onUserSessionChanged(userSession: UserSession)
}