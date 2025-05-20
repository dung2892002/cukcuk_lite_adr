package com.example.app.mvp_modules.statistic.views

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.FragmentStatisticBinding
import com.example.app.datas.CukcukDbHelper
import com.example.app.datas.repositories.StatisticRepository
import com.example.app.dto.StatisticByInventory
import com.example.app.dto.StatisticByTime
import com.example.app.dto.StatisticOverview
import com.example.app.mvp_modules.statistic.adapters.StatisticByInventoryAdapter
import com.example.app.mvp_modules.statistic.adapters.StatisticByTimeAdapter
import com.example.app.mvp_modules.statistic.adapters.StatisticOverviewAdapter
import com.example.app.mvp_modules.statistic.contracts.StatisticContract
import com.example.app.mvp_modules.statistic.presenters.StatisticPresenter
import com.example.app.utils.ChartUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@SuppressLint("NewApi")
class StatisticFragment : Fragment(), StatisticContract.View {
    private lateinit var binding: FragmentStatisticBinding
    private lateinit var presenter: StatisticContract.Presenter
    private var statisticsByTime = mutableListOf<StatisticByTime>()
    private var statisticsByInventory = mutableListOf<StatisticByInventory>()
    private var statisticOverview = mutableListOf<StatisticOverview>()
    private lateinit var adapterByTime: StatisticByTimeAdapter
    private lateinit var adapterByInventory: StatisticByInventoryAdapter
    private lateinit var adapterOverview: StatisticOverviewAdapter
    private lateinit var dialog: AlertDialog
    private lateinit var selectDateDialog: AlertDialog
    private var selectedStartDate = LocalDate.now().withDayOfMonth(1) // đầu tháng
    private var selectedEndDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()) // cuối tháng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)

        val db = CukcukDbHelper(requireContext())
        val repository = StatisticRepository(db)
        presenter = StatisticPresenter(this, repository)

        presenter.statisticOverview()

        binding.header.setOnClickListener {
            showSelectTimeStatisticDialog()
        }

        return binding.root
    }

    private fun showSelectTimeStatisticDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_select_time_statistic, null)
        dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialogView.findViewById<TextView>(R.id.selectOverview).setOnClickListener {
            dialog.dismiss()
            presenter.statisticOverview()
        }

        dialogView.findViewById<TextView>(R.id.selectCurrentWeek).setOnClickListener {
            closeDialog()
            presenter.statisticCurrentWeek()
        }

        dialogView.findViewById<TextView>(R.id.selectPreviousWeek).setOnClickListener {
            closeDialog()
            presenter.statisticPreviousWeek()
        }

        dialogView.findViewById<TextView>(R.id.selectCurrentMonth).setOnClickListener {
            closeDialog()
            presenter.statisticCurrentMonth()
        }

        dialogView.findViewById<TextView>(R.id.selectPreviousMonth).setOnClickListener {
            closeDialog()
            presenter.statisticPreviousMonth()
        }

        dialogView.findViewById<TextView>(R.id.selectCurrentYear).setOnClickListener {
            closeDialog()
            presenter.statisticCurrentYear()
        }

        dialogView.findViewById<TextView>(R.id.selectPreviousYear).setOnClickListener {
            closeDialog()
            presenter.statisticPreviousYear()
        }

        dialogView.findViewById<TextView>(R.id.selectDateToDate).setOnClickListener {
            closeDialog()
            showDialogSelectDateToDate()
        }

        dialog.show()
    }

    @SuppressLint("NewApi")
    private fun showDialogSelectDateToDate() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_select_datetodate, null)
        selectDateDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()


        val tvDateStart = dialogView.findViewById<TextView>(R.id.dateStart)
        val tvDateEnd = dialogView.findViewById<TextView>(R.id.dateEnd)

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        tvDateStart.text = selectedStartDate.format(formatter)
        tvDateEnd.text = selectedEndDate.format(formatter)

        dialogView.findViewById<LinearLayout>(R.id.openDateStartPicker).setOnClickListener {
            val date = selectedStartDate
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                selectedStartDate = LocalDate.of(year, month + 1, day)
                tvDateStart.text = selectedStartDate.format(formatter)
            },
                date.year, date.monthValue - 1, date.dayOfMonth).show()
        }

        dialogView.findViewById<LinearLayout>(R.id.openDateEndPicker).setOnClickListener {
            val date = selectedEndDate
            DatePickerDialog(
                requireContext(),
                R.style.CustomDatePickerDialogTheme,
                { _, year, month, day ->
                selectedEndDate = LocalDate.of(year, month + 1, day)
                tvDateEnd.text = selectedEndDate.format(formatter)
            },
                date.year, date.monthValue - 1, date.dayOfMonth).show()
        }


        dialogView.findViewById<AppCompatButton>(R.id.btnCancelStatisticDateToDate).setOnClickListener {
            selectDateDialog.dismiss()
        }

        dialogView.findViewById<AppCompatButton>(R.id.btnSubmitStatisticDateToDate).setOnClickListener {
            val startDateTime = selectedStartDate.atTime(0, 0, 0)
            val endDateTime = selectedEndDate.atTime(23, 59, 59)
            selectDateDialog.dismiss()
            presenter.statisticInventoryDateToDate(startDateTime, endDateTime)
        }

        selectDateDialog.show()
    }

    private fun closeDialog() {
        dialog.dismiss()
    }

    private fun setupAdapterOverview() {
        adapterOverview = StatisticOverviewAdapter(requireContext(), statisticOverview).apply {
            onItemClick = {item, position ->
                presenter.handleClickItemOverview(item, position)
            }
        }
        binding.recyclerStatisticOverview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerStatisticOverview.adapter = adapterOverview


    }

    private fun setupAdapterByTime() {
        adapterByTime = StatisticByTimeAdapter(requireContext(), statisticsByTime).apply {
            onItemClick = {item ->
                presenter.handleNavigateByTime(item)
            }
        }
        binding.recyclerStatisticByTime.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerStatisticByTime.adapter = adapterByTime
    }

    private fun setupAdapterByInventory() {
        adapterByInventory = StatisticByInventoryAdapter(requireContext(), statisticsByInventory)
        binding.recyclerStatisticByInventory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerStatisticByInventory.adapter = adapterByInventory
    }


    override fun showStatisticOverview(items: List<StatisticOverview>) {
        statisticOverview = items.toMutableList()

        binding.statisticLabel.text = "Gần đây"
        binding.recyclerStatisticOverview.visibility = View.VISIBLE
        binding.statisticByTime.visibility = View.GONE
        binding.statisticByInventory.visibility = View.GONE

        setupAdapterOverview()
    }

    override fun showStatisticByTime(
        items: List<StatisticByTime>,
        label: String,
        xChartLabels: List<String>,
        type: Int
    ) {
        statisticsByTime = items.toMutableList()

        binding.statisticLabel.text = label
        binding.recyclerStatisticOverview.visibility = View.GONE
        binding.statisticByTime.visibility = View.VISIBLE
        binding.statisticByInventory.visibility = View.GONE

        if (statisticsByTime.isEmpty()) {
            binding.statisticByTimeNoData.visibility = View.VISIBLE
            binding.statisticByTimeHasData.visibility = View.GONE
        } else {
            binding.statisticByTimeNoData.visibility = View.GONE
            binding.statisticByTimeHasData.visibility = View.VISIBLE
            setupAdapterByTime()
            if (type == 2) {
                binding.labelOxChartLine.text = getString(R.string.label_ox_month)
            } else {
                binding.labelOxChartLine.text = getString(R.string.label_ox)
            }
            ChartUtils.setupLineChart(binding.chartByTime, items, xChartLabels)  {
                when (type) {
                    0 -> it.TimeStart.dayOfWeek.value - 1 // Theo tuần
                    1 -> it.TimeStart.dayOfMonth - 1      // Theo tháng
                    2 -> it.TimeStart.monthValue - 1      // Theo năm
                    else -> 0
                }
            }
        }
    }

    override fun showStatisticByInventory(items: List<StatisticByInventory>, label: String, totalAmount: Double) {
        statisticsByInventory = items.toMutableList()
        binding.statisticLabel.text = label
        binding.recyclerStatisticOverview.visibility = View.GONE
        binding.statisticByTime.visibility = View.GONE
        binding.statisticByInventory.visibility = View.VISIBLE

        if (statisticsByInventory.isEmpty()) {
            binding.statisticByInventoryNoData.visibility = View.VISIBLE
            binding.statisticByInventoryHasData.visibility = View.GONE
        } else {
            binding.statisticByInventoryNoData.visibility = View.GONE
            binding.statisticByInventoryHasData.visibility = View.VISIBLE
            ChartUtils.setupPieChart(binding.chartByInventory, items, totalAmount)
            setupAdapterByInventory()
        }
    }

    override fun navigateToStatisticInventoryActivity(
        start: LocalDateTime,
        end: LocalDateTime,
        label: String,
    ) {
        var intent = Intent(requireContext(), StatisticByInventoryActivity::class.java)
        intent.putExtra("date_start", start.toString())
        intent.putExtra("date_end", end.toString())
        intent.putExtra("time_label", label)

        startActivity(intent)
    }
}