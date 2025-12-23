package com.purenative.services.location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

class LocationManagerProxy: NSObject(), CLLocationManagerDelegateProtocol {
    private val manager: CLLocationManager by lazy {
        val manager = CLLocationManager()
        manager.desiredAccuracy = kCLLocationAccuracyBest
        manager.delegate = this
        manager
    }

    private var onReceiveSingleLocationCallback: ((Location?) -> Unit)? = null

    @OptIn(ExperimentalForeignApi::class)
    private fun requestCurrentUserLocation(onReceiveLocation: (Location?) -> Unit) {
        manager.location?.coordinate?.useContents {
            val lastUserLocation = Location(latitude, longitude)
            onReceiveLocation(lastUserLocation)
        } ?: run {
            onReceiveSingleLocationCallback = onReceiveLocation
            dispatch_async(dispatch_get_main_queue(), {
                manager.requestLocation()
            })
        }
    }

    suspend fun currentUserLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            requestCurrentUserLocation { location ->
                continuation.resume(
                    value = location,
                    onCancellation = { _, _, _ -> }
                )
            }
        }

    private var onReceiveAuthorizationStatusCallback: ((CLAuthorizationStatus) -> Unit)? = null

    private fun requestWhenInUseAuthorization(onReceiveAuthorizationStatus: ((CLAuthorizationStatus) -> Unit)) {
        val authorizationStatus = CLLocationManager.authorizationStatus()
        when (authorizationStatus) {
            kCLAuthorizationStatusNotDetermined -> {
                onReceiveAuthorizationStatusCallback = onReceiveAuthorizationStatus
                dispatch_async(dispatch_get_main_queue(), {
                    manager.requestWhenInUseAuthorization()
                })
            }

            else -> onReceiveAuthorizationStatus(authorizationStatus)
        }
    }

    suspend fun requestAuthorizationStatus(): CLAuthorizationStatus =
        suspendCancellableCoroutine { continuation ->
            requestWhenInUseAuthorization { authorizationStatus ->
                continuation.resume(
                    value = authorizationStatus,
                    onCancellation = { _, _, _ -> }
                )
            }
        }

    override fun locationManager(
        manager: CLLocationManager,
        didChangeAuthorizationStatus: CLAuthorizationStatus
    ) {
        when (didChangeAuthorizationStatus) {
            kCLAuthorizationStatusNotDetermined -> return

            else -> {
                onReceiveAuthorizationStatusCallback?.invoke(didChangeAuthorizationStatus)
                onReceiveAuthorizationStatusCallback = null
            }
        }
    }

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        proxyLocation(didUpdateLocations.firstOrNull() as? CLLocation)
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        proxyLocation(null)
    }

    private fun proxyLocation(location: CLLocation?) {
        val location = location?.location()
        onReceiveSingleLocationCallback?.invoke(location)
        onReceiveSingleLocationCallback = null
    }
}