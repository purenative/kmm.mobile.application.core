package com.purenative.application.alert

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMAlertActionConfiguration", "AlertActionConfiguration", true)
data class AlertActionConfiguration(
    val id: String = "",
    val title: String,
    val style: AlertActionStyle = AlertActionStyle.NORMAL
)