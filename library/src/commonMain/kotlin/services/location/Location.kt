package com.purenative.services.location

data class Location(
    val latitude: Double,
    val longitude: Double
): Locatable {
    override val location = this

    fun distanceTo(location: Location): Double =
        location_distanceBetween(this, location)
}

internal expect fun location_distanceBetween(firstLocation: Location, secondLocation: Location): Double