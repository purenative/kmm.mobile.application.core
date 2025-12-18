package com.purenative.services.location

import platform.CoreLocation.kCLAuthorizationStatusAuthorized
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse

actual object LocationService {
    private val locationManager by lazy { LocationManagerProxy() }
    private val geocoder by lazy { GeocoderProxy() }

    actual suspend fun currentUserLocation(): Location? =
        when (locationManager.requestAuthorizationStatus()) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways,
            kCLAuthorizationStatusAuthorized -> locationManager.currentUserLocation()

            else -> null
        }

    actual suspend fun forwardGeocoding(address: String): Location? =
        geocoder.forwardGeocoding(address)

    actual suspend fun reverseGeocoding(location: Location): String? =
        geocoder.reverseGeocoding(location)
}