package com.purenative.services.permission

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.purenative.application.ApplicationContextHolder
import java.lang.ref.WeakReference
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object PermissionService {
    private val context by lazy { ApplicationContextHolder.applicationContext }
    private val permissionCallbacks = mutableMapOf<String, (Boolean) -> Unit>()
    private var providerRef: WeakReference<PermissionRequestProvider>? = null

    fun set(provider: PermissionRequestProvider) {
        providerRef = WeakReference(provider)
    }

    fun notifyPermission(
        permissionName: String,
        isAccessGranted: Boolean
    ) {
        val callback = permissionCallbacks.remove(permissionName)
        callback?.let { it(isAccessGranted) }
    }

    suspend fun requestPermission(permissionName: String): Boolean {
        val permissionStatus = ContextCompat.checkSelfPermission(context, permissionName)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) return true

        val provider = providerRef?.get() ?: return false

        return requestPermissionFromProvider(
            permissionName,
            provider
        )
    }

    private suspend fun requestPermissionFromProvider(
        permissionName: String,
        provider: PermissionRequestProvider
    ): Boolean = 
        suspendCoroutine { continuation ->
            requestPermissionFromProvider(
                permissionName,
                provider,
                continuation::resume
            )
        }

    private fun requestPermissionFromProvider(
        permissionName: String,
        provider: PermissionRequestProvider,
        callback: (Boolean) -> Unit
    ) {
        permissionCallbacks[permissionName] = callback
        provider.permissionRequest.launch(arrayOf(permissionName))
    }
}