package com.purenative.application

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraphBuilder
import com.purenative.application.alert.AlertConfiguration
import com.purenative.application.banner.BannerConfiguration
import com.purenative.application.banner.BannerPresenter
import com.purenative.application.navigator.ApplicationNavigation
import com.purenative.application.navigator.ApplicationNavigationHandler
import com.purenative.application.navigator.ApplicationNavigator
import com.purenative.application.screen.ScreenRoute
import com.purenative.services.permission.PermissionRequestProvider
import com.purenative.services.permission.PermissionService
import com.purenative.services.pushnotification.PushNotificationService
import com.purenative.system.IntentProxy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class ApplicationActivity:
    ComponentActivity(),
    PermissionRequestProvider,
    ApplicationNavigationHandler
{
    override val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsStatus ->
            for ((permissionName, isAccessGranted) in permissionsStatus) {
                PermissionService.notifyPermission(permissionName, isAccessGranted)
            }
        }

    // FixMe:
    //  Почему-то с первого раза ивент не транслируется
    //  во flow. Приходится в первый раз слать 2 ивента.
    private var navigationEventPassed = false
    private val navigationEventsChannel = Channel<ApplicationNavigation>()
    private val bannerEventsChannel = Channel<BannerConfiguration?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        ApplicationNavigator.set(this)
        PermissionService.set(this)
        IntentProxy.set(this)

        BannerPresenter.onBannerUpdated = {
            lifecycleScope.launch {
                bannerEventsChannel.send(it)
            }
        }
    }

    fun setApplicationContent(
        background: @Composable (darkTheme: Boolean) -> Color = { Color.Transparent },
        rootRouteBuilder: NavGraphBuilder.(startDestination: String, selectedTabFlow: Flow<Int>) -> Unit,
        routesBuilder: NavGraphBuilder.() -> Unit,
        alertContent: @Composable (alertConfiguration: AlertConfiguration, onDismissRequest: () -> Unit) -> Unit = { _, _ -> },
        bannerContent: @Composable (bannerConfiguration: BannerConfiguration) -> Unit = { },
    ) {
        setContent {
            ApplicationContent(
                Modifier.background(background(isSystemInDarkTheme())),
                navigationEventsChannel.receiveAsFlow(),
                bannerEventsChannel.receiveAsFlow(),
                rootRouteBuilder,
                routesBuilder,
                alertContent,
                bannerContent,
                { PushNotificationService.engine.handleNewIntent(intent) }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        PushNotificationService.engine.handleNewIntent(intent)
    }

    override fun route(route: ScreenRoute) {
        sendNavigationEvent(ApplicationNavigation.ApplicationNavigationOpenScreen(route))
    }

    override fun showAlert(alertConfiguration: AlertConfiguration?) {
        sendNavigationEvent(ApplicationNavigation.ApplicationNavigationPresentAlert(alertConfiguration))
    }

    override fun changeTab(index: Int) {
        sendNavigationEvent(ApplicationNavigation.ApplicationNavigationChangeTab(index))
    }

    private fun sendNavigationEvent(navigation: ApplicationNavigation) {
        lifecycleScope.launch {
            if (!navigationEventPassed) {
                navigationEventPassed = true
                navigationEventsChannel.send(navigation)
            }
            navigationEventsChannel.send(navigation)
        }
    }
}