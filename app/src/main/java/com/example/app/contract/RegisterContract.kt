package com.example.app.contract

interface RegisterContract {
    interface View {
        fun showRegisterSuccess()
        fun showRegisterFailed()
    }

    interface Presenter {
        fun handleRegisterAccount(username: String, password: String)
    }
}