package com.test.loginapp.data.repos.implimentations

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.test.loginapp.data.repos.LoginRepo
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepoImpl @Inject constructor(
    private val fbAuth: FirebaseAuth
): LoginRepo {

    override suspend fun login(email: String, password: String): AuthResult =
        fbAuth.signInWithEmailAndPassword(email, password).await() ?:
        throw FirebaseAuthException("", "")

}