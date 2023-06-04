package com.dolla.mrimate.util

/**
 * @created 17/01/2023 - 1:43 AM
 * @project MRIMate
 * @author adell
 */
inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
    }
}