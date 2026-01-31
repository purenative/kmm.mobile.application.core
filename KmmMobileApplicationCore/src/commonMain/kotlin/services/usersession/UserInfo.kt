package com.purenative.services.usersession

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val userId: String,
    val userName: String,
    val userRole: String
)