package com.purenative.application.navigator

import com.purenative.application.alert.AlertConfiguration
import com.purenative.application.screen.ScreenRoute
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMApplicationNavigator", "ApplicationNavigator", true)
object ApplicationNavigator {
    private var handler: ApplicationNavigationHandler? = null

    fun set(handler: ApplicationNavigationHandler) {
        this.handler = handler
    }

    fun navigate(navigation: ApplicationNavigation) =
        when (navigation) {
            is ApplicationNavigation.ApplicationNavigationOpenScreen -> route(navigation.route)
            is ApplicationNavigation.ApplicationNavigationPresentAlert -> showAlert(navigation.alertConfiguration)
            is ApplicationNavigation.ApplicationNavigationChangeTab -> changeTab(navigation.index)
        }

    fun route(route: ScreenRoute) =
        handler?.route(route)

    fun showAlert(alertConfiguration: AlertConfiguration?) =
        handler?.showAlert(alertConfiguration)

    fun changeTab(index: Int) =
        handler?.changeTab(index)
}