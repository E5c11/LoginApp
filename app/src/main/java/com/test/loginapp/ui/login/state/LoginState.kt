package com.test.loginapp.ui.login.state

data class LoginState(
    val loading: Boolean = false,
    val loginMessage: String = "",
    val error: String = ""
)