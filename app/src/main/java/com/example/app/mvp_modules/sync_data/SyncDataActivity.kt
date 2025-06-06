package com.example.app.mvp_modules.sync_data

import android.os.Bundle
import android.text.SpannableString
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.app.R
import com.example.app.databinding.ActivitySyncDataBinding
import com.example.app.datas.CukcukDbHelper
import kotlinx.coroutines.launch

class SyncDataActivity : AppCompatActivity(), SyncContract.View {
    private lateinit var binding: ActivitySyncDataBinding
    private lateinit var presenter: SyncContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySyncDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = CukcukDbHelper(this)
        presenter = SyncPresenter(this, db)

        setupToolbar()
        lifecycleScope.launch {
            presenter.getSyncData()
        }

        binding.btnSync.setOnClickListener {
            presenter.handleSyncData()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbarTitle.text = buildString {
            append("Đồng bộ dữ liệu")
        }
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

    override fun showSyncData(
        lastSyncTime: SpannableString,
        countSync: SpannableString,
    ) {
        binding.lastSyncTime.text = lastSyncTime
        binding.countSync.text = countSync
    }

    override fun toggleSyncCountGroup(state: Boolean) {
        binding.syncCountGroup.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun showLoading() {
        binding.loadingOverlay.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loadingOverlay.visibility = View.GONE
    }
}