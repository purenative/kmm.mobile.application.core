package com.purenative.application.banner

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMBannerType", "BannerType", true)
enum class BannerType {
    INFO, ERROR
}