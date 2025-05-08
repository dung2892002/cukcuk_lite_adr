package com.example.app.mvp_modules.login_with_account

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.databinding.ActivityLoginAccountBinding
import com.example.app.mvp_modules.login.screens.AppInfo

class LoginAccountActivity : AppCompatActivity(), LoginAccountContract.View {
    private lateinit var binding: ActivityLoginAccountBinding
    private lateinit var presenter: LoginAccountContract.Presenter
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = LoginAccountPresenter(this)

        setupToolbar()
        setupInput()
        handleLogin()
        handleForgetPassword()
        goAppInfo()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun goAppInfo() {
        binding.btnAppInfo.setOnClickListener {
            val intent = Intent(this, AppInfo::class.java)
            startActivity(intent)
        }
    }


    private fun setupInput() {
        binding.edtUsername.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.btnClearUsername.visibility = View.VISIBLE
            }
            else {
                binding.btnClearUsername.visibility = View.GONE
            }
        }

        binding.edtPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.btnClearPassword.visibility = View.VISIBLE
            }
            else {
                binding.btnClearPassword.visibility = View.GONE
            }
        }

        binding.btnClearUsername.setOnClickListener {
            binding.edtUsername.setText("")
        }

        binding.btnClearPassword.setOnClickListener {
            binding.edtPassword.setText("")
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.loginAccountToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_login_account_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("InflateParams")
    private fun handleForgetPassword() {
        binding.txtForgetPassword.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.forgot_password, null)
            dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create()

            dialogView.findViewById<ImageButton>(R.id.btnExitForgetPassword).setOnClickListener {
                dialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.btnSubmitForgetPassword).setOnClickListener {
                val value = dialogView.findViewById<EditText>(R.id.edtForgotPassword).text.toString()
                presenter.sendForgotPassword(value)
            }

            dialog.show()
        }

    }

    private fun handleLogin() {
        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()
            presenter.login(username, password)
        }
    }

    override fun handleLoginSuccess(username: String, password: String) {
        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()

        val sharedPref = getSharedPreferences("Auth", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("username", username)
            putString("password", password)
            apply()
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showForgotPasswordMessage(message: String, state: Boolean) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        if (state) dialog.dismiss()
    }

}