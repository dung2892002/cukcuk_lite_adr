package com.example.app.mvp_modules.register

import com.example.app.utils.ValidatorUtils

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
        if (!ValidatorUtils.isEmailOrPhone(username)) return "Email hoặc số điện thoại sai định dạng"
        if (!ValidatorUtils.isValidPassword(password)) return "Mật khẩu phải từ 8 kí tự, có chữ hoa, chữ thường, chữ số và kí tự đặc biệt"
        return null
    }


}