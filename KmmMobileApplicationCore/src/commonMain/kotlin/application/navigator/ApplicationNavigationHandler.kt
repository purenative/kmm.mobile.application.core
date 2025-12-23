package com.purenative.application.navigator

import com.purenative.application.alert.AlertConfiguration
import com.purenative.application.screen.ScreenRoute
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMApplicationNavigationHandler", "ApplicationNavigationHandler", true)
interface ApplicationNavigationHandler {
    fun route(route: ScreenRoute)
    fun showAlert(alertConfiguration: AlertConfiguration?)
    fun changeTab(index: Int)
}