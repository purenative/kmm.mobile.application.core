package com.purenative.application

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.purenative.application.alert.AlertConfiguration
import com.purenative.application.banner.BannerConfiguration
import com.purenative.application.navigator.ApplicationNavigation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

@Composable
internal fun ApplicationContent(
    modifier: Modifier,
    navigationFlow: Flow<ApplicationNavigation>,
    bannerFlow: Flow<BannerConfiguration?>,
    rootRouteBuilder: NavGraphBuilder.(startDestination: String, selectedTabFlow: Flow<Int>) -> Unit,
    routesBuilder: NavGraphBuilder.() -> Unit,
    alertContent: @Composable (alertConfiguration: AlertConfiguration, onDismissRequest: () -> Unit) -> Unit,
    bannerContent: @Composable (bannerConfiguration: BannerConfiguration) -> Unit,
    onLaunch: () -> Unit
) {
    val startDestination = "application.root.screen"

    val navController = rememberNavController()
    var alertConfiguration by remember { mutableStateOf<AlertConfiguration?>(null) }
    var bannerConfiguration by remember { mutableStateOf<BannerConfiguration?>(null) }

    val selectedTabFlow = navigationFlow
        .mapNotNull { navigation ->
            when (navigation) {
                is ApplicationNavigation.ApplicationNavigationChangeTab -> navigation.index
                else -> null
            }
        }

    val graph = navController.createGraph(startDestination) {
        rootRouteBuilder(startDestination, selectedTabFlow)
        routesBuilder()
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        graph = graph,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    )

    alertConfiguration?.let { configuration ->
        alertContent(
            configuration,
            { alertConfiguration = null }
        )
    }

    ApplicationBannerOverlay(
        bannerConfiguration,
        bannerContent,
        { bannerConfiguration = null }
    )

    LaunchedEffect(Unit) {
        navigationFlow.collect { navigation ->
            when (navigation) {
                is ApplicationNavigation.ApplicationNavigationOpenScreen ->
                    navController.navigate(navigation.route)

                is ApplicationNavigation.ApplicationNavigationChangeTab ->
                    return@collect

                is ApplicationNavigation.ApplicationNavigationPresentAlert ->
                    alertConfiguration = navigation.alertConfiguration
            }
        }
    }

    LaunchedEffect(Unit) {
        bannerFlow.collect { configuration ->
            bannerConfiguration = configuration
        }
    }

    LaunchedEffect(Unit) {
        onLaunch()
    }
}