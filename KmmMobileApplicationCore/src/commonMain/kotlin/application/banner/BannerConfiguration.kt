package com.purenative.application.banner

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMBannerConfiguration", "BannerConfiguration", true)
data class BannerConfiguration(
    val type: BannerType,
    val title: String
)