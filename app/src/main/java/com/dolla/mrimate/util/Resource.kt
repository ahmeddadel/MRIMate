package com.dolla.mrimate.util

/**
 * @created 17/01/2023 - 1:45 AM
 * @project MRIMate
 * @author adell
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}
