package com.purenative.application.screen

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMScreenRouteOpening", "ScreenRouteOpening", true)
enum class ScreenRouteOpening {
    PUSH, PRESENT
}