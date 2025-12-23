package com.purenative.services.location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLPlacemark

class GeocoderProxy {
    private val geocoder = CLGeocoder()

    @OptIn(ExperimentalForeignApi::class)
    private fun forwardGeocoding(address: String, onReceiveLocation: (Location?) -> Unit) {
        geocoder.cancelGeocode()

        if (address.isEmpty()) {
            onReceiveLocation(null)
            return
        }

        geocoder.geocodeAddressString(
            addressString = address,
            completionHandler = { placemarks, error ->
                val placemark = placemarks?.firstOrNull() as? CLPlacemark
                val location = placemark?.location?.location()
                onReceiveLocation(location)
            }
        )
    }

    suspend fun forwardGeocoding(address: String): Location? =
        suspendCancellableCoroutine { continuation ->
            forwardGeocoding(address) { location ->
                continuation.resume(
                    value = location,
                    onCancellation = { _, _, _ -> }
                )
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    private fun reverseGeocoding(location: Location, onReceiveAddress: (String?) -> Unit) {
        geocoder.cancelGeocode()
        geocoder.reverseGeocodeLocation(
            location = location.location(),
            completionHandler = { placemarks, error ->
                val placemark = placemarks?.firstOrNull() as? CLPlacemark
                placemark?.let { placemark ->
                    onReceiveAddress(placemark.addressDescription())
                } ?: run {
                    onReceiveAddress(null)
                }
            }
        )
    }

    suspend fun reverseGeocoding(location: Location): String? =
        suspendCancellableCoroutine { continuation ->
            reverseGeocoding(location) { address ->
                continuation.resume(
                    value = address,
                    onCancellation = { _, _, _ -> }
                )
            }
        }
}

private fun CLPlacemark.addressDescription(): String? {
    val components = mutableListOf<String>()

    locality?.let { locality ->
        components.add(locality)
    } ?: return null

    thoroughfare?.let { thoroughfare ->
        subThoroughfare?.let { subThoroughfare ->
            components.add("$thoroughfare $subThoroughfare")
        } ?: run {
            components.add(thoroughfare)
        }
    } ?: return null

    val addressDescription = components.joinToString(separator = ", ")
    return addressDescription
}