package com.example.app.mvp_modules.register

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.R
import com.example.app.databinding.ActivityRegisterBinding
import androidx.core.net.toUri

class RegisterActivity : AppCompatActivity(), RegisterContract.View {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var presenter: RegisterContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = RegisterPresenter(this)

        setupToolbar()
        handleRegister()
        filterColorFooter()
        handleClickFooter()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleClickFooter() {
        binding.txtFooterContent.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, "https://www.cukcuk.vn".toUri())
            startActivity(intent)
        }
    }

    private fun filterColorFooter() {
        val text = binding.txtFooterContent.text.toString()
        val spannable = SpannableString(text)

        val start = text.indexOf("thỏa thuận")
        val end = text.length

        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.bg_login_via_account)), // hoặc Color.RED
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.txtFooterContent.text = spannable
    }

    private fun handleRegister() {
        binding.btnSubmitRegister.setOnClickListener {
            submitRegisterForm()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.registerToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Đăng ký"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.register_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.btnSubmitRegisterToolbar -> {
                submitRegisterForm()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showRegisterSuccess() {
        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun showRegisterFailed(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun submitRegisterForm() {
        val username = binding.edtRegisterUsername.text.toString().trim()
        val password = binding.edtRegisterPassword.text.toString().trim()
        presenter.handleRegisterAccount(username, password)
    }

}