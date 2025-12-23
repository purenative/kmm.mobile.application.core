package com.purenative.application.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified VIEW_MODEL: BaseScreenViewModel> Screen(
    noinline topBar: (@Composable (ScreenTopBarCofiguration) -> Unit)?,
    backgroundColor: Color,
    crossinline viewModelInitializer: CreationExtras.() -> VIEW_MODEL,
    crossinline content: @Composable (VIEW_MODEL) -> Unit
) {
    val viewModel = viewModel<VIEW_MODEL> { viewModelInitializer() }

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

    Column(modifier = Modifier.background(backgroundColor)) {
        topBar?.let {
            val configuration = ScreenTopBarCofiguration(
                viewModel.screenTitle,
                viewModel.actions
            )
            it(configuration)
        }

        content(viewModel)
    }
}