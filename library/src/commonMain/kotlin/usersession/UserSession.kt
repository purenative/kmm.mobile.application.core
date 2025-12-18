package com.purenative.usersession

data class UserSession(val userInfo: UserInfo? = null) {
    fun isAuthorized(): Boolean = userInfo != null
}
