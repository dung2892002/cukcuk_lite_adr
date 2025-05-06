package com.example.app.presenters

import android.os.Handler
import android.os.Looper
import com.example.app.contract.LoginAccountContract

class LoginAccountPresenter(private val view: LoginAccountContract.View) : LoginAccountContract.Presenter {

    override fun login(username: String, password: String) {
        view.showLoading()

        // Giả lập xử lý đăng nhập
        Handler(Looper.getMainLooper()).postDelayed({
            view.hideLoading()
            if (username == "admin" && password == "123456") {
                view.showLoginSuccess()
            } else {
                view.showLoginError("Sai tài khoản hoặc mật khẩu")
            }
        }, 1500)
    }

    override fun sendForgotPassword(emailOrPhone: String) {
        if (emailOrPhone.isEmpty()) {
            view.showForgotPasswordMessage("Vui lòng nhập email hoặc số điện thoại")
        } else {
            // Gửi đến server, giả lập phản hồi
            view.showForgotPasswordMessage("Đã gửi đến: $emailOrPhone")
        }
    }
}
