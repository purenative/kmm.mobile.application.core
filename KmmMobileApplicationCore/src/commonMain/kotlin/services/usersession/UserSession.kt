package com.purenative.services.usersession

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMUserSession", "UserSession", true)
data class UserSession(val userInfo: UserInfo? = null) {
    fun isAuthorized(): Boolean = userInfo != null
}
