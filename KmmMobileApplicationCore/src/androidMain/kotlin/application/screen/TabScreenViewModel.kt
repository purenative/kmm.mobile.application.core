package com.purenative.application.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.purenative.services.usersession.UserSession
import com.purenative.services.usersession.UserSessionObserver
import com.purenative.services.usersession.UserSessionService

open class TabScreenViewModel<Tab>(
    startTab: Tab,
    internal val tabs: List<Tab>
):
    BaseScreenViewModel(),
    UserSessionObserver
{
    internal var selectedTab by mutableStateOf(startTab)
        private set
    internal var isUserAuthorized by mutableStateOf(UserSessionService.isUserAuthorized)
        private set

    override fun onCreate() {
        UserSessionService.addObserver(this)
    }
    override fun onAppear() { }
    override fun onDisappear() { }
    override fun onDestroy() {
        UserSessionService.removeObserver(this)
    }

    override val userSessionObserverIdentifier: String
        get() = "TabScreenViewModel"

    override fun onUserSessionChanged(userSession: UserSession) {
        isUserAuthorized = userSession.isAuthorized()
    }

    fun selectTab(tabIndex: Int) {
        selectedTab = tabs[tabIndex]
    }

    fun selectTab(tab: Tab) {
        selectedTab = tab
    }
}