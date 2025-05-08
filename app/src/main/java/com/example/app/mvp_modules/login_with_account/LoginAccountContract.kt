package com.example.app.mvp_modules.login_with_account

interface LoginAccountContract {
    interface View {
        fun handleLoginSuccess(username: String, password: String)
        fun showLoginError(message: String)
        fun showForgotPasswordMessage(message: String, state: Boolean)
    }

    interface Presenter {
        fun login(username: String, password: String)
        fun sendForgotPassword(emailOrPhone: String)
    }
}