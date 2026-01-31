package com.purenative.application.alert

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMAlertConfiguration", "AlertConfiguration", true)
data class AlertConfiguration(
    val title: String?,
    val message: String? = null,
    val style: AlertStyle = AlertStyle.NORMAL,
    val actions: List<AlertActionConfiguration>,
    val onActionTapped: (String) -> Unit
) {
    val alertTitle: String? = title ?: message
    val alertMessage: String? = if (title.isNullOrEmpty()) null else message
}