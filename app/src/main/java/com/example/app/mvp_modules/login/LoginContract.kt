package com.example.app.mvp_modules.login

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface LoginContract {
    interface View {
        fun navigateToLoginViaAccount()
        fun navigateToRegister()
        fun showMessage(message: String)

    }

    interface Presenter {
        fun onLoginViaAccountClicked()
        fun onRegisterClicked()
        fun onGoogleSignInClicked(context: Context)
        fun onFaceBookSignInClicked(context: Context)
    }
}
