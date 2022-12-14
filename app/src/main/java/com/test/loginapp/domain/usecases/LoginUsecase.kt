package com.test.loginapp.domain.usecases

import com.google.firebase.auth.FirebaseAuthException
import com.test.loginapp.data.preferences.LoginDetails
import com.test.loginapp.data.preferences.UserPreferences
import com.test.loginapp.data.repos.LoginRepo
import com.test.loginapp.util.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUsecase @Inject constructor(
    private val userPref: UserPreferences,
    private val loginRepo: LoginRepo
) {

    suspend operator fun invoke(email: String, password: String): Flow<Task<LoginDetails>> = flow{
        emit(Task.Loading())
        userPref.loginPref.collect {
            if (it.email.isNotEmpty())
            try {
                val user = loginRepo.login(email, password).user
                // update user pref
                if (user != null) emit(Task.Success(
                    LoginDetails(user.email!!, password, user.uid)
                ))
                else emit(Task.Error("User not created"))
            } catch (e: FirebaseAuthException) {
                emit(Task.Error("An error has occurred"))
                e.stackTrace
            }
        }
    }




}