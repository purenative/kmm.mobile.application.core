package com.purenative.services.location

import kotlinx.coroutines.suspendCancellableCoroutine
import android.Manifest
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.Priority
import com.google.android.gms.location.LocationServices
import com.purenative.services.permission.PermissionService

class LocationClientProxy {
    @androidx.annotation.RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ])
    suspend fun currentUserLocation(context: Context): Location? = suspendCancellableCoroutine  { continuation ->
        val client = getLocationClient(context)
        val priority = Priority.PRIORITY_HIGH_ACCURACY
        client.getCurrentLocation(priority, null)
            .addOnSuccessListener { location ->
                val location = location?.location()
                continuation.resume(
                    value = location,
                    onCancellation = { _, _, _ -> }
                )
            }
            .addOnFailureListener { exception ->
                continuation.resume(
                    value = null,
                    onCancellation = { _, _, _ -> }
                )
            }
    }

    suspend fun requestAccessFineLocation(): Boolean =
        PermissionService.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)

    suspend fun requestAccessCoarseLocation(): Boolean =
        PermissionService.requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION)

    private fun getLocationClient(context: Context) =
        LocationServices.getFusedLocationProviderClient(context)

    private fun requestLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gpsEnabled || networkEnabled
    }
}