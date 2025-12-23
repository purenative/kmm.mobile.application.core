package com.purenative.services.location

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMLocatable", "Locatable", true)
interface Locatable {
    val location: Location
}