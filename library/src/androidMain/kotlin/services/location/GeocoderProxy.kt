package com.purenative.services.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class GeocoderProxy {
    suspend fun forwardGeocoding(context: Context, address: String): Location? =
        suspendCoroutine { continuation ->
            if (address.isEmpty()) {
                continuation.resume(null)
            } else {
                val location = forwardGeocodingSync(context, address)
                continuation.resume(location)
            }
        }

    private fun forwardGeocodingSync(context: Context, address: String): Location? =
        try {
            Geocoder(context).getFromLocationName(
                address,
                1
            )?.firstOrNull()?.location()
        } catch (e: Exception) {
            null
        }

    suspend fun reverseGeocoding(context: Context, location: Location): String? =
        suspendCoroutine { continuation ->
            val address = reverseGeocodingSync(context, location)
            continuation.resume(address)
        }

    private fun reverseGeocodingSync(context: Context, location: Location): String? =
        try {
            Geocoder(context).getFromLocation(
                location.latitude,
                location.longitude,
                1,
            )?.firstOrNull()?.addressDescription()
        } catch (e: Exception) {
            null
        }
}

private fun Address.addressDescription(): String? {
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

private fun Address.location(): Location =
    Location(latitude, longitude)