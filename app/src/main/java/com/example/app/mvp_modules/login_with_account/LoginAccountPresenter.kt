package com.example.app.mvp_modules.login_with_account

import android.os.Handler
import android.os.Looper
import com.example.app.utils.ValidatorUtils

class LoginAccountPresenter(private val view: LoginAccountContract.View) : LoginAccountContract.Presenter {

    override fun login(username: String, password: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (username == "dung2002ts" && password == "Dung2002ts*") {
                view.handleLoginSuccess(username, password)
            } else {
                view.showLoginError("Sai tài khoản hoặc mật khẩu")
            }
        }, 500)
    }

    override fun sendForgotPassword(input: String) {
        if (input.isEmpty()) {
            view.showForgotPasswordMessage("Vui lòng nhập email hoặc số điện thoại", false)
            return
        }

        if(!ValidatorUtils.isEmailOrPhone(input)) {
            view.showForgotPasswordMessage("Số điện thoại hoặc email không đúng định dạng", false)
            return
        }

        view.showForgotPasswordMessage("Đã gửi yêu cầu đến sever: $input", true)
    }
}
