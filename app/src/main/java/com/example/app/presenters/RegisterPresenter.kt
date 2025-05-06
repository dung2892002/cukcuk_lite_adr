package com.example.app.presenters

import com.example.app.contract.RegisterContract

class RegisterPresenter(private val view: RegisterContract.View) : RegisterContract.Presenter {
    override fun handleRegisterAccount(username: String, password: String) {
        if (checkRequest(username, password)) {
            view.showRegisterSuccess()
        }
        else {
            view.showRegisterFailed()
        }
    }

    private fun checkRequest(username: String, password: String) : Boolean {
        if (!checkPassword(password)) return false
        if (!isEmailOrPhone(username)) return false
        return true
    }

    private fun checkPassword(password: String) : Boolean {
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}\$")
        return passwordRegex.matches(password)
    }

    private fun isEmailOrPhone(input: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
        val phoneRegex = Regex("^\\d{9,15}\$") // từ 9 đến 15 chữ số

        return emailRegex.matches(input) || phoneRegex.matches(input)
    }

}