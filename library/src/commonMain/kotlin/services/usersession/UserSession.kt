package com.purenative.services.usersession

data class UserSession(val userInfo: UserInfo? = null) {
    fun isAuthorized(): Boolean = userInfo != null
}
