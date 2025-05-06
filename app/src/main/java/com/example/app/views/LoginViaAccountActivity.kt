package com.example.app.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.R
import com.example.app.contract.LoginAccountContract
import com.example.app.databinding.ActivityLoginViaAccountBinding
import com.example.app.presenters.LoginAccountPresenter

class LoginViaAccountActivity : AppCompatActivity(), LoginAccountContract.View {
    private lateinit var binding: ActivityLoginViaAccountBinding
    private lateinit var presenter: LoginAccountContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginViaAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = LoginAccountPresenter(this)

        setupToolbar()
        handleLogin()
        handleForgetPassword()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create()

            dialogView.findViewById<ImageButton>(R.id.btnExitForgetPassword).setOnClickListener {
                dialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.btnSubmitForgetPassword).setOnClickListener {
                val value = dialogView.findViewById<EditText>(R.id.edtForgotPassword).text.toString()

                if (value.isEmpty()) {
                    Toast.makeText(this, "Nhap sdt or email", Toast.LENGTH_SHORT).show()
                }
                else {
                    presenter.sendForgotPassword(value)
                    Toast.makeText(this, "Send data to sever: $value", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
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



    override fun showLoading() {
        Toast.makeText(this, "Đang xử lý...", Toast.LENGTH_SHORT).show()
    }

    override fun hideLoading() {
        // Nếu dùng ProgressDialog hoặc ProgressBar thì ẩn tại đây, tạm thời không cần
    }

    override fun showLoginSuccess() {
        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
        // TODO: Chuyển sang màn hình chính nếu cần
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showForgotPasswordMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}