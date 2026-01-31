package com.purenative.application.navigator

import com.purenative.application.alert.AlertConfiguration
import com.purenative.application.screen.ScreenRoute
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMApplicationNavigation", "ApplicationNavigation", true)
sealed class ApplicationNavigation {
    data class ApplicationNavigationOpenScreen(val route: ScreenRoute): ApplicationNavigation()
    data class ApplicationNavigationChangeTab(val index: Int): ApplicationNavigation()
    data class ApplicationNavigationPresentAlert(val alertConfiguration: AlertConfiguration?): ApplicationNavigation()
}