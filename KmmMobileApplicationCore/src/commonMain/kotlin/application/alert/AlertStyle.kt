package com.purenative.application.alert

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMAlertStyle", "AlertStyle", true)
enum class AlertStyle {
    NORMAL, SHEET
}