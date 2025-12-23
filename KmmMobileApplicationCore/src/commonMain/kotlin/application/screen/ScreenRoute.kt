package com.purenative.application.screen

import kotlinx.serialization.Serializable
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMScreenRoute", "ScreenRoute", true)
@Serializable
open class ScreenRoute(
    open val screenTitle: String? = null,
    open val opening: ScreenRouteOpening = ScreenRouteOpening.PUSH
)