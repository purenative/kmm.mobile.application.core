package com.purenative.application.alert

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMAlertActionStyle", "AlertActionStyle", true)
enum class AlertActionStyle {
    NORMAL, CANCEL, DESTRUCTIVE
}