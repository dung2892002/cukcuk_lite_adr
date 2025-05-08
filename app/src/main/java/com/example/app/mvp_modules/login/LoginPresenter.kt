package com.example.app.mvp_modules.login

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginPresenter(private val view: LoginContract.View) : LoginContract.Presenter {

    override fun onLoginViaAccountClicked() {
        view.navigateToLoginViaAccount()
    }

    override fun onRegisterClicked() {
        view.navigateToRegister()
    }

    override fun onGoogleSignInClicked(context: Context) {
        Toast.makeText(context, "Login google", Toast.LENGTH_SHORT).show()
    }

    override fun onFaceBookSignInClicked(context: Context) {
        Toast.makeText(context, "Login facebook", Toast.LENGTH_SHORT).show()
    }
}
