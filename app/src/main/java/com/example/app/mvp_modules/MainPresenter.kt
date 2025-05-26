package com.example.app.mvp_modules

import android.annotation.SuppressLint
import android.content.Context
import com.example.app.R
import com.example.app.datas.CukcukDbHelper
import com.example.app.datas.repositories.SyncRepository
import com.example.app.mvp_modules.app_info.AppInfoActivity
import com.example.app.mvp_modules.link_account.LinkAccountActivity
import com.example.app.mvp_modules.login.LoginActivity
import com.example.app.mvp_modules.notification.NotificationActivity
import com.example.app.mvp_modules.set_password.SetPasswordActivity
import com.example.app.mvp_modules.setting.SettingActivity
import com.example.app.mvp_modules.suggestion.SuggestionActivity
import com.example.app.mvp_modules.sync_data.SyncDataActivity

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

    override fun checkLogin(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("Auth", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "")
        val password = sharedPref.getString("password", "")
        return username == "dung2002ts" && password == "Dung2002ts*"
    }

    override fun handleNavigationItemSelected(itemId: Int) {
        when (itemId) {
            R.id.nav_sale -> view.switchFragment("Bán hàng")
            R.id.nav_menu -> view.switchFragment("Thực đơn")
            R.id.nav_statistic -> view.switchFragment("Doanh thu")
            R.id.nav_sync_data -> view.openActivity(SyncDataActivity::class.java)
            R.id.nav_setting -> view.openActivity(SettingActivity::class.java)
            R.id.nav_link_account -> view.openActivity(LinkAccountActivity::class.java)
            R.id.nav_notification -> view.openActivity(NotificationActivity::class.java)
            R.id.nav_suggestion -> view.openActivity(SuggestionActivity::class.java)
            R.id.nav_app_info -> view.openActivity(AppInfoActivity::class.java)
            R.id.nav_password -> view.openActivity(SetPasswordActivity::class.java)
            R.id.nav_share -> view.showToast("Share with friend")
            R.id.nav_rating -> view.showToast("Rating app")
            R.id.nav_logout -> logout(view as Context)
        }

        view.closeDrawer()
    }

    @SuppressLint("UseKtx")
    override fun logout(context: Context) {
        val sharedPref = context.getSharedPreferences("Auth", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("username")
            remove("password")
            apply()
        }
        view.openActivity(LoginActivity::class.java)
    }

    override fun getSyncCount(): Int {
        val repository = SyncRepository(CukcukDbHelper(view as Context))
        return repository.countSync()
    }
}