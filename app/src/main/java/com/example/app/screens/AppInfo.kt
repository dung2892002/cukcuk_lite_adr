package com.example.app.screens

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.R
import com.example.app.adapters.AppInfoAdapter
import com.example.app.databinding.ActivityAppInfoBinding

class AppInfo : AppCompatActivity() {
    private lateinit var binding: ActivityAppInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAppInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListView()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupListView() {
        val items = mutableListOf<String>()
        items.add("Cập nhật thực đơn của quán")
        items.add("Ghi nhận các món khách gọi")
        items.add("Tính tiền cho khách")
        items.add("Xem thống kê doanh thu bán hàng theo thời gian")
        items.add("Xem thống kê doanh thu bán hàng theo mặt hàng")

        val adapter = AppInfoAdapter(this, items)
        binding.lvAppInfos.adapter = adapter
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.appInfoToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_info_menu, menu);
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
}