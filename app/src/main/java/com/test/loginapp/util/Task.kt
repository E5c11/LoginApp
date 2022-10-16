package com.test.loginapp.util

sealed class Task<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T): Task<T>(data)
    class Error<T>(message: String, data: T? = null): Task<T>(data, message)
    class Loading<T>(data: T? = null): Task<T>(data)
}