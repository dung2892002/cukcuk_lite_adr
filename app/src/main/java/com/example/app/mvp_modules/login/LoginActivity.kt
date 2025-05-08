package com.example.app.mvp_modules.login

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.R
import com.example.app.databinding.ActivityLoginBinding
import com.example.app.mvp_modules.login_with_account.LoginAccountActivity
import com.example.app.mvp_modules.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginActivity : AppCompatActivity(), LoginContract.View {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var presenter: LoginContract.Presenter
    private val RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = LoginPresenter(this)

        val googleSignInClient = GoogleSignIn.getClient(this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build())

        binding.btnLoginViaGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        binding.btnLoginViaAccount.setOnClickListener {
            presenter.onLoginViaAccountClicked()
        }

        binding.btnGoRegister.setOnClickListener {
            presenter.onRegisterClicked()
        }

        handleResizeTextButton()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleResizeTextButton() {
        val text = binding.txtTextResize.text.toString()
        val spannable = SpannableString(text)

        spannable.setSpan(
            RelativeSizeSpan(0.8f), // 80% size hiện tại
            0,
            "Đăng nhập bằng".length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.txtTextResize.text = spannable
    }


    override fun navigateToLoginViaAccount() {
        val intent = Intent(this, LoginAccountActivity::class.java)
        startActivity(intent)
    }

    override fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showGoogleSignInSuccess(account: GoogleSignInAccount) {
        Toast.makeText(this, "Đăng nhập thành công: ${account.displayName}", Toast.LENGTH_SHORT).show()
    }

    override fun showGoogleSignInError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        if (requestCode == RC_SIGN_IN) {
            presenter.handleGoogleSignInResult(data)
        }
    }
}