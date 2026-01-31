package com.purenative.application

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.purenative.application.banner.BannerConfiguration

@Composable
internal fun ApplicationBannerOverlay(
    bannerConfiguration: BannerConfiguration?,
    bannerContent: @Composable (bannerConfiguration: BannerConfiguration) -> Unit,
    onBannerDismissed: () -> Unit
) {
    Box(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        var offsetY by remember { mutableIntStateOf(0) }
        var bannerHeight by remember { mutableIntStateOf(0) }

        AnimatedVisibility(
            bannerConfiguration != null,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it }
        ) {
            bannerConfiguration?.let {
                Box(
                    Modifier
                        .offset { IntOffset(0, offsetY.coerceIn(-bannerHeight, 0)) }
                        .onSizeChanged { newSize ->
                            bannerHeight = newSize.height
                        }
                        .pointerInput(Unit) {
                            val velocityTracker = VelocityTracker()

                            detectDragGestures(
                                onDragStart = {
                                    velocityTracker.resetTracking()
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    velocityTracker.addPosition(change.uptimeMillis, change.position)
                                    offsetY = offsetY + dragAmount.y.toInt()
                                },
                                onDragEnd = {
                                    val velocityY = velocityTracker.calculateVelocity().y
                                    val dismissingNeededByVelocity = velocityY != 0f && offsetY < 0
                                    val dismissingNeededByOffset = offsetY < -bannerHeight / 2
                                    val dismissingNeeded = dismissingNeededByVelocity || dismissingNeededByOffset

                                    if (dismissingNeeded) {
                                        onBannerDismissed()
                                    }
                                    offsetY = 0
                                }
                            )
                        }
                        .padding(16.dp)
                ) {
                    bannerContent(it)
                }
            }
        }
    }
}