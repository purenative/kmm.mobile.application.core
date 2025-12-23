package com.purenative.application.banner

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMBannerPresenter", "BannerPresenter", true)
object BannerPresenter {
    var onBannerUpdated: ((BannerConfiguration?) -> Unit)? = null

    fun presentBanner(configuration: BannerConfiguration) {
        onBannerUpdated?.invoke(configuration)
    }

    fun hideBanner() {
        onBannerUpdated?.invoke(null)
    }
}