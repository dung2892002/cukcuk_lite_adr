package com.example.app.mvp_modules.statistic.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.ActivityStatisticByInventoryBinding
import com.example.app.datas.CukcukDbHelper
import com.example.app.datas.repositories.StatisticRepository
import com.example.app.dto.StatisticByInventory
import com.example.app.mvp_modules.statistic.adapters.StatisticByInventoryAdapter
import com.example.app.mvp_modules.statistic.contracts.StatisticByInventoryContract
import com.example.app.mvp_modules.statistic.presenters.StatisticByInventoryPresenter
import com.example.app.utils.ChartUtils
import java.time.LocalDateTime

class StatisticByInventoryActivity : AppCompatActivity(), StatisticByInventoryContract.View {
    private lateinit var binding: ActivityStatisticByInventoryBinding
    private lateinit var presenter: StatisticByInventoryContract.Presenter
    private lateinit var dateStart: LocalDateTime
    private lateinit var dateEnd: LocalDateTime
    private lateinit var timeLabel: String
    private lateinit var statisticsData: List<StatisticByInventory>
    private lateinit var adapter: StatisticByInventoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStatisticByInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = CukcukDbHelper(this)
        val repository = StatisticRepository(db)
        presenter = StatisticByInventoryPresenter(this, repository)

        setupToolbar()
        getDateRequest()

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
        supportActionBar?.title = ""
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

    @SuppressLint("NewApi")
    private fun getDateRequest() {
        dateStart = LocalDateTime.parse(intent.getStringExtra("date_start"))
        dateEnd = LocalDateTime.parse(intent.getStringExtra("date_end"))
        timeLabel = intent.getStringExtra("time_label")!!
        binding.statisticTimeLabel.text = timeLabel

        presenter.statisticInventoryDateToDate(dateStart, dateEnd)
    }

    override fun showDataRecycler(items: List<StatisticByInventory>, totalAmount: Double) {
        statisticsData = items
        setupAdapter()
        setupChart(items, totalAmount)
    }

    private fun setupChart(items: List<StatisticByInventory>, totalAmount: Double) {
        ChartUtils.setupPieChart(binding.chartByInventory, items, totalAmount)
    }

    private fun setupAdapter() {
        adapter = StatisticByInventoryAdapter(this, statisticsData.toMutableList())
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter
    }
}