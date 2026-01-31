package com.purenative.services

class ServiceException(
    val reason: ServiceFailureReason,
    val baseException: Exception,
    val customLocalizedMessage: String? = null
): Exception() {
    fun localizedMessage(): String = customLocalizedMessage ?: reason.localizedMessage()
}