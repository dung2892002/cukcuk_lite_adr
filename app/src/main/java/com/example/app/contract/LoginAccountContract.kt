package com.example.app.contract

interface LoginAccountContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun showLoginSuccess()
        fun showLoginError(message: String)
        fun showForgotPasswordMessage(message: String)
    }

    interface Presenter {
        fun login(username: String, password: String)
        fun sendForgotPassword(emailOrPhone: String)
    }
}