package com.purenative.services.permission

import androidx.activity.result.ActivityResultLauncher

interface PermissionRequestProvider {
    val permissionRequest: ActivityResultLauncher<Array<String>>
}