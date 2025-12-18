package com.purenative.usersession

interface UserSessionObserver {
    val userSessionObserverIdentifier: String

    fun onUserSessionChanged(userSession: UserSession)
}