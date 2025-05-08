package com.example.app.mvp_modules.login_with_account

import android.os.Handler
import android.os.Looper

class LoginAccountPresenter(private val view: LoginAccountContract.View) : LoginAccountContract.Presenter {

    override fun login(username: String, password: String) {
        view.showLoading()
        // Giả lập xử lý đăng nhập
        Handler(Looper.getMainLooper()).postDelayed({
            view.hideLoading()
            if (username == "dung2002ts" && password == "Dung2002ts*") {
                view.showLoginSuccess(username, password)
            } else {
                view.showLoginError("Sai tài khoản hoặc mật khẩu")
            }
        }, 500)
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
