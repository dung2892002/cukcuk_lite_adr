package com.example.app.presenters

import com.example.app.contract.RegisterContract

class RegisterPresenter(private val view: RegisterContract.View) : RegisterContract.Presenter {
    override fun handleRegisterAccount(username: String, password: String) {
        val messageFail = checkRequest(username, password)
        if (messageFail.isNullOrEmpty()) {
            view.showRegisterSuccess()
        }
        else {
            view.showRegisterFailed(messageFail)
        }
    }

    private fun checkRequest(username: String, password: String) : String? {
        if (username.isEmpty()) return "Số điện thoại hoặc email không được để trống"
        if (password.isEmpty()) return "Mật khẩu không được để trống"
        if (!isEmailOrPhone(username)) return "Email hoặc số điện thoại sai định dạng"
        if (!checkPassword(password)) return "Mật khẩu phải từ 8 kí tự, có chữ hoa, chữ thường, chữ số và kí tự đặc biệt"
        return null
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