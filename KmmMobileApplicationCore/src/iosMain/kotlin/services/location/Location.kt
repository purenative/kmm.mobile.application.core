package com.purenative.services.location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents

typealias IOSLocation = platform.CoreLocation.CLLocation

fun Location.location(): IOSLocation =
    IOSLocation(latitude, longitude)

@OptIn(ExperimentalForeignApi::class)
fun IOSLocation.location(): Location =
    coordinate.useContents {
        Location(latitude, longitude)
    }

actual fun location_distanceBetween(firstLocation: Location, secondLocation: Location): Double =
    firstLocation.location().distanceFromLocation(secondLocation.location())