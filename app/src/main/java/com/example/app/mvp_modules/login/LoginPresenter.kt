package com.example.app.mvp_modules.login

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

class LoginPresenter(private val view: LoginContract.View) : LoginContract.Presenter {

    override fun onLoginViaAccountClicked() {
        view.navigateToLoginViaAccount()
    }

    override fun onRegisterClicked() {
        view.navigateToRegister()
    }

    override fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                view.showGoogleSignInSuccess(account)
            } else {
                view.showGoogleSignInError("Tài khoản không hợp lệ")
            }
        } catch (e: ApiException) {
            view.showGoogleSignInError("${e.message}")
        }
    }
}
