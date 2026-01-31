package com.purenative.application.screen

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect

data class TabContentConfiguration<Tab>(
    val selectedTab: Tab,
    val isUserAuthorized: Boolean,
    val contentPadding: PaddingValues
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <Tab> TabScreen(
    viewModel: TabScreenViewModel<Tab>,
    backgroundColor: Color,
    content: @Composable (TabContentConfiguration<Tab>) -> Unit,
    tabBar: @Composable (Tab, List<Tab>, onTabSelected: (Tab) -> Unit) -> Unit
) {
    LifecycleStartEffect(Unit) {
        viewModel.onCreate()

        onStopOrDispose {
            viewModel.onDestroy()
        }
    }
    LifecycleResumeEffect(Unit) {
        viewModel.onAppear()

        onPauseOrDispose {
            viewModel.onDisappear()
        }
    }

    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.ime.only(WindowInsetsSides.Bottom)),
        content = { scaffoldPadding ->
            val configuration = TabContentConfiguration(
                viewModel.selectedTab,
                viewModel.isUserAuthorized,
                scaffoldPadding
            )
            content(configuration)
        },
        bottomBar = {
            val isKeyboardOpened =
                WindowInsets.isImeVisible

            if (!isKeyboardOpened) {
                tabBar(
                    viewModel.selectedTab,
                    viewModel.tabs,
                    { viewModel.selectTab(it) }
                )
            }
        },
        containerColor = backgroundColor
    )
}