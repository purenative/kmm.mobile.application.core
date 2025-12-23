package com.purenative.services.location

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMLocation", "Location", true)
data class Location(
    val latitude: Double,
    val longitude: Double
): Locatable {
    override val location = this

    fun distanceTo(location: Location): Double =
        location_distanceBetween(this, location)
}

internal expect fun location_distanceBetween(firstLocation: Location, secondLocation: Location): Double