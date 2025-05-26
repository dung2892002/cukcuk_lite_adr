package com.example.app.mvp_modules.sync_data

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.example.app.datas.CukcukDbHelper
import com.example.app.datas.repositories.InventoryRepository
import com.example.app.datas.repositories.InvoiceRepository
import com.example.app.datas.repositories.SyncRepository
import com.example.app.datas.repositories.UnitRepository
import com.example.app.utils.FormatDisplay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

@SuppressLint("NewApi")
class SyncPresenter(private val view: SyncContract.View,
    private val dbHelper: CukcukDbHelper) : SyncContract.Presenter {

    private val syncRepository = SyncRepository(dbHelper)
    private val unitRepository = UnitRepository(dbHelper)
    private val inventoryRepository = InventoryRepository(dbHelper)
    private val invoiceRepository = InvoiceRepository(dbHelper)

    override fun getSyncData() {
        val lastSyncTime = syncRepository.getLastSyncTime()
        val countSync = syncRepository.countSync()

        var lastSyncTimeBuilder = SpannableString("Chưa thực hiện đồng bộ dữ liệu!!")
        var countSyncBuilder = SpannableString("")

        if (countSync > 0) {
            view.toggleSyncCountGroup(true)
            countSyncBuilder = createCountSyncSpannable(countSync)
        } else {
            view.toggleSyncCountGroup(false)
        }
        if (lastSyncTime != null) lastSyncTimeBuilder = createLastSyncTimeSpannable(lastSyncTime)
        view.showSyncData(lastSyncTimeBuilder, countSyncBuilder)
    }


    override fun handleSyncData() {
        CoroutineScope(Dispatchers.Main).launch {
            view.showLoading()

            withContext(Dispatchers.IO) {
                delay(3000)
                val syncs = syncRepository.getAllSync()
                for (sync in syncs) {
                    when (sync.TableName) {
                        "Unit" -> unitRepository.getUnitById(sync.ObjectID)
                        "Inventory" -> inventoryRepository.getInventoryById(sync.ObjectID)
                        "Invoice" -> invoiceRepository.getInvoiceById(sync.ObjectID)
                        "InvoiceDetail" -> invoiceRepository.getInvoiceDetailById(sync.ObjectID)
                    }
                }
                syncRepository.deleteRange(syncs.map { it.SynchronizeID })
                syncRepository.updateLastSyncTime(LocalDateTime.now())
            }

            getSyncData()
            view.hideLoading()
        }
    }

    private fun createCountSyncSpannable(countSync: Int): SpannableString {
        val startString = "Bạn đang có "
        val countValue = countSync.toString()
        val endString =
            " giao dịch chưa được đồng bộ. Các thiết bị khác sẽ không nhận được những thay đổi này. Vui lòng kiểm tra kết nối mạng và thực hiện đồng bộ."

        val fullText = startString + countValue + endString
        val spannable = SpannableString(fullText)

        // Màu xám cho startString
        spannable.setSpan(
            ForegroundColorSpan(Color.GRAY),
            0,
            startString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Màu đỏ cho countValue
        val countStart = startString.length
        val countEnd = countStart + countValue.length
        spannable.setSpan(
            ForegroundColorSpan(Color.RED),
            countStart,
            countEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Màu xám cho endString
        val endStart = countEnd
        val endEnd = fullText.length
        spannable.setSpan(
            ForegroundColorSpan(Color.GRAY),
            endStart,
            endEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannable
    }


    private fun createLastSyncTimeSpannable(lastSyncTime: LocalDateTime?): SpannableString {
        val label = "Đồng bộ lần cuối: "
        val timeString = FormatDisplay.formatTo12HourWithCustomAMPM(lastSyncTime.toString())

        val spannable = SpannableStringBuilder()
        spannable.append(label)

        val start = spannable.length
        spannable.append(timeString)
        val end = spannable.length

        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return SpannableString(spannable)
    }



}