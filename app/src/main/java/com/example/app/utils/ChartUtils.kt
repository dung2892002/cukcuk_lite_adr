package com.example.app.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import com.example.app.dto.StatisticByInventory
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import androidx.core.graphics.toColorInt
import com.example.app.dto.StatisticByTime
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

object ChartUtils {
    @SuppressLint("DefaultLocale")
    fun setupPieChart(pieChart: PieChart, items: List<StatisticByInventory>, totalAmount: Double) {
        val sorted = items.sortedByDescending { it.Percentage }
        val mainParts = sorted.take(6)
        val otherParts = sorted.drop(6)
        val otherTotal = otherParts.sumOf { it.Percentage }

        val entries = mutableListOf<PieEntry>()
        val colors = mutableListOf<Int>()

        mainParts.forEach {
            entries.add(PieEntry(it.Percentage.toFloat(), "${String.format("%.1f", it.Percentage)}%"))
            colors.add(it.Color.toColorInt())
        }

        if (otherParts.isNotEmpty()) {
            entries.add(PieEntry(otherTotal.toFloat(), "${String.format("%.1f", otherTotal)}%"))
            colors.add(Color.GRAY)
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors

        // ✨ Hiển thị giá trị bên ngoài lát cắt và đường nối
        dataSet.setDrawValues(true)
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f
        dataSet.valueFormatter = PercentFormatter(pieChart)

        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.valueLinePart1Length = 0.5f
        dataSet.valueLinePart2Length = 0.3f
        dataSet.valueLineColor = Color.BLACK
        dataSet.valueLineWidth = 1f

        val pieData = PieData(dataSet)

        pieChart.apply {
            data = pieData
            description.isEnabled = false
            legend.isEnabled = false
            setUsePercentValues(true)
            setDrawEntryLabels(false)

            setHoleColor(Color.WHITE)
            isDrawHoleEnabled = true
            holeRadius = 75f
            transparentCircleRadius = 80f

            centerText = generateCenterText(totalAmount)
            setCenterTextSize(16f)
            setCenterTextColor(Color.BLACK)

            setExtraOffsets(5f, 10f, 5f, 10f)

            invalidate()
            animateY(1000, Easing.EaseInOutQuad)
        }
    }

    fun setupLineChart(
        chart: LineChart,
        statisticList: List<StatisticByTime>,
        xLabels: List<String>,
        labelMapper: (StatisticByTime) -> Int
    ) {

        val amountMap = mutableMapOf<Int, Float>()
        statisticList.forEach {
            val index = labelMapper(it)
            amountMap[index] = it.Amount.toFloat()
        }

        val entries = statisticList.map {
            val index = labelMapper(it)
            Entry(index.toFloat(), (it.Amount / 1000).toInt().toFloat())
        }



        val dataSet = LineDataSet(entries, null).apply {
            color = Color.GREEN
            setCircleColors(Color.GREEN)
            setDrawCircleHole(false) // Tắt lỗ trắng, điểm sẽ là hình tròn đặc
            valueTextColor = Color.BLACK
            lineWidth = 1f
            setDrawCircles(true)
            setDrawValues(false)
            mode = LineDataSet.Mode.LINEAR
        }

        chart.data = LineData(dataSet)

        chart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(xLabels)
            labelCount = if (xLabels.size <= 12) xLabels.size else 10
            granularity = 1f
            axisMinimum = 0f
            axisMaximum = (xLabels.size - 1).toFloat()
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setDrawLabels(true)
        }

        val maxAmount = statisticList.maxOfOrNull { it.Amount } ?: 0.0
        val roundedMax = ((maxAmount / 100000).toInt() + 1) * 100

        chart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = roundedMax.toFloat()
            setDrawAxisLine(false)
            setDrawLabels(true)
            setDrawGridLines(true)
            setDrawZeroLine(false)
            zeroLineColor = Color.GRAY
            zeroLineWidth = 0.5f
            enableGridDashedLine(10f, 5f, 0f)
        }



        chart.legend.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
        chart.invalidate()
    }


    private fun generateCenterText(totalAmount: Double): SpannableString {
        val line1 = "Tổng\ndoanh thu\n"
        val line2 = FormatDisplay.formatNumber(totalAmount.toString())
        val fullText = line1 + line2

        val spannable = SpannableString(fullText)

        spannable.setSpan(
            AbsoluteSizeSpan(30, true),
            0,
            line1.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            AbsoluteSizeSpan(40, true),
            line1.length,
            fullText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }
}