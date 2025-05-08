package com.example.app.mvp_modules.register

interface RegisterContract {
    interface View {
        fun showRegisterSuccess()
        fun showRegisterFailed(message:String)
    }

    interface Presenter {
        fun handleRegisterAccount(username: String, password: String)
    }
}