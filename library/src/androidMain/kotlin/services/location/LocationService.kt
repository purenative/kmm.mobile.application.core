package com.purenative.services.location

import com.purenative.application.ApplicationContextHolder

actual object LocationService {
    private val context by lazy { ApplicationContextHolder.applicationContext }
    private val locationClient by lazy { LocationClientProxy() }
    private val geocoder by lazy { GeocoderProxy() }

    @androidx.annotation.RequiresPermission(allOf = [
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ])
    actual suspend fun currentUserLocation(): Location? =
        if (locationClient.requestAccessFineLocation() && locationClient.requestAccessCoarseLocation())
            locationClient.currentUserLocation(context)
        else
            null

    actual suspend fun forwardGeocoding(address: String): Location? =
        geocoder.forwardGeocoding(context, address)

    actual suspend fun reverseGeocoding(location: Location): String? =
        geocoder.reverseGeocoding(context, location)
}