package com.test.loginapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.loginapp.domain.usecases.LoginUsecase
import com.test.loginapp.ui.login.state.DetailState
import com.test.loginapp.ui.login.state.LoginState
import com.test.loginapp.util.Constants.NUMBERS
import com.test.loginapp.util.Constants.SPECIAL_CHARS
import com.test.loginapp.util.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUsecase: LoginUsecase
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState
    private val _detailState = MutableStateFlow(DetailState())
    val detailState = _detailState
    private var email = ""
    private var password = ""

    fun login() = viewModelScope.launch {
        if (email.isNotEmpty() && password.isNotEmpty()) loginUsecase(email, password).onEach {
            when(it) {
                is Task.Success -> {
                    _loginState.value = LoginState(
                        loginMessage = "You are logged in with ${it.data?.email}",
                        loading = false
                    )
                }
                is Task.Loading -> {
                    _loginState.value = LoginState(loading = true)
                }
                is Task.Error -> {
                    _loginState.value = LoginState(error = "${it.message}")
                }
            }
        }
        else _detailState.value = DetailState(
            emailValid = "Please enter a valid email",
            passwordValid = "Please enter a valid password"
        )
    }



    fun checkEmail(e: String) {
        if (e.isNotEmpty() && e.contains("@") && e.contains(".")) {
            _detailState.value = DetailState()
            email = e
        }
        else _detailState.value = DetailState(emailValid = "Please enter a valid email")
    }

    fun checkPassword(p: String) {
        if (p.isNotEmpty() && p.contains(NUMBERS.toRegex()) && p.contains(SPECIAL_CHARS.toRegex())) {
            _detailState.value = DetailState()
            password = p
        }
        else _detailState.value = DetailState(passwordValid = "Please enter a valid password")
    }

}