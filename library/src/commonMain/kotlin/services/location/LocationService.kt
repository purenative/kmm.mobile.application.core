package com.purenative.services.location

expect object LocationService {
    suspend fun currentUserLocation(): Location?
    suspend fun forwardGeocoding(address: String): Location?
    suspend fun reverseGeocoding(location: Location): String?
}