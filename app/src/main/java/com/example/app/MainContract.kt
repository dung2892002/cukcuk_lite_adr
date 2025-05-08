package com.example.app

import android.content.Context

interface MainContract {
    interface View {
        fun openLoginScreen()
        fun showUserInfo(fullName: String, username: String)
        fun switchFragment(title: String)
        fun closeDrawer()
        fun openActivity(activityClass: Class<*>)
        fun showToast(message: String)
    }

    interface Presenter {
        fun checkLogin(context: Context): Boolean
        fun handleNavigationItemSelected(itemId: Int)
        fun logout(context: Context)
    }
}