package com.test.loginapp.data.repos

import com.google.firebase.auth.AuthResult

interface LoginRepo {

    suspend fun login(email: String, password: String): AuthResult

}