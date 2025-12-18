package com.purenative.services.usersession

interface UserSessionObserver {
    val userSessionObserverIdentifier: String

    fun onUserSessionChanged(userSession: UserSession)
}