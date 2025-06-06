package com.example.app.mvp_modules.sync_data

import android.text.SpannableString

interface SyncContract {
    interface View{
        fun showSyncData(lastSyncTime: SpannableString, countSync: SpannableString)
        fun toggleSyncCountGroup(state: Boolean)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter{
        suspend fun getSyncData()
        fun handleSyncData()
    }
}