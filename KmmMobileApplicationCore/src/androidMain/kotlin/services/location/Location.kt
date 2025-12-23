package com.purenative.services.location

typealias AndroidLocation = android.location.Location

fun Location.location(): AndroidLocation =
    AndroidLocation(null).apply {
        latitude = this@location.latitude
        longitude = this@location.longitude
    }

fun AndroidLocation.location(): Location =
    Location(latitude, longitude)

actual fun location_distanceBetween(firstLocation: Location, secondLocation: Location): Double =
    firstLocation.location().distanceTo(secondLocation.location()).toDouble()