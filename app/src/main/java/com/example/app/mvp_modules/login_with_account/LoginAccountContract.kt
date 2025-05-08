package com.example.app.mvp_modules.login_with_account

interface LoginAccountContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun showLoginSuccess(username: String, password: String)
        fun showLoginError(message: String)
        fun showForgotPasswordMessage(message: String)
    }

    interface Presenter {
        fun login(username: String, password: String)
        fun sendForgotPassword(emailOrPhone: String)
    }
}