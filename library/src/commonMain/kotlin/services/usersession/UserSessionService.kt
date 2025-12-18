package com.purenative.services.usersession

import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json

object UserSessionService {
    private const val SESSION_USER_INFO_KEY = "com_purenative_session_user_info"

    private val settings = Settings()
    private var observers = hashMapOf<String, UserSessionObserver>()

    val isUserAuthorized: Boolean
        get() = readUserSession().isAuthorized()

    val userId: String?
        get() = readUserSession().userInfo?.userId

    val userName: String?
        get() = readUserSession().userInfo?.userName

    fun updateUserSession(userSession: UserSession) {
        saveUserSession(userSession)
        notifyObservers(userSession)
    }

    fun addObserver(observer: UserSessionObserver) {
        observers[observer.userSessionObserverIdentifier] = observer
        observer.onUserSessionChanged(readUserSession())
    }

    fun removeObserver(observer: UserSessionObserver) {
        observers.remove(observer.userSessionObserverIdentifier)
    }

    private fun notifyObservers(userSession: UserSession) {
        for ((_, value) in observers) {
            value.onUserSessionChanged(userSession)
        }
    }

    private fun saveUserSession(userSession: UserSession) {
        val json = Json { ignoreUnknownKeys = true }
        val jsonString = userSession.userInfo?.let { json.encodeToString(it) }
        settings.putString(SESSION_USER_INFO_KEY, jsonString ?: "")
    }

    private fun readUserSession(): UserSession {
        val jsonString = settings.getStringOrNull(SESSION_USER_INFO_KEY) ?: return UserSession()
        try {
            val json = Json { ignoreUnknownKeys = true }
            val userInfo = json.decodeFromString<UserInfo>(jsonString)
            val userSession = UserSession(userInfo)
            return userSession
        } catch (e: Exception) {
            return UserSession()
        }
    }
}