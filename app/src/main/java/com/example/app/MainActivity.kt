package com.example.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.app.databinding.ActivityMainBinding
import com.example.app.mvp_modules.menu.MenuFragment
import com.example.app.mvp_modules.sale.SaleFragment
import com.example.app.mvp_modules.statistic.StatisticFragment
import androidx.core.view.size
import androidx.core.view.get
import com.example.app.databinding.NavHeaderBinding
import com.example.app.mvp_modules.app_info.AppInfoActivity
import com.example.app.mvp_modules.link_account.LinkAccountActivity
import com.example.app.mvp_modules.login.LoginActivity
import com.example.app.mvp_modules.notification.NotificationActivity
import com.example.app.mvp_modules.set_password.SetPasswordActivity
import com.example.app.mvp_modules.setting.SettingActivity
import com.example.app.mvp_modules.suggestion.SuggestionActivity
import com.example.app.mvp_modules.sync_data.SyncDataActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var headerBinding: NavHeaderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!checkLogin()) {
            openActivity(LoginActivity::class.java)
        } else {
            setupToolbar()
            setupNavigation()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkLogin() : Boolean {
        val sharedPref = getSharedPreferences("Auth", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "")
        val password = sharedPref.getString("password", "")

        return username == "dung2002ts" && password == "Dung2002ts*"
    }

    private fun setupNavigation() {

        headerBinding = NavHeaderBinding.bind(binding.navigationView.getHeaderView(0))

        "Lê Văn Dũng".also { headerBinding.txtNavHeaderFullName.text = it }
        "vandung2002ts@gmail.com".also { headerBinding.txtNavHeaderUsername.text = it }

        val navMenu = binding.navigationView.menu
        for (i in 0 until navMenu.size) {
            val item = navMenu[i]
            if (item.hasSubMenu()) {
                val styledTitle = SpannableString(item.title)
                styledTitle.setSpan(ForegroundColorSpan(Color.GRAY), 0, styledTitle.length, 0)
                styledTitle.setSpan(AbsoluteSizeSpan(14, true), 0, styledTitle.length, 0)
                item.title = styledTitle
            }
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.nav_sale -> {
                    switchFragment(SaleFragment(), "Bán hàng", R.id.nav_sale)
                }
                R.id.nav_menu -> {
                    switchFragment(MenuFragment(), "Thực đơn", R.id.nav_menu)
                }
                R.id.nav_statistic -> {
                    switchFragment(StatisticFragment(), "Thống kê", R.id.nav_statistic)
                }
                R.id.nav_sync_data -> {
                    openActivity(SyncDataActivity::class.java)
                }
                R.id.nav_setting -> {
                    openActivity(SettingActivity::class.java)
                }
                R.id.nav_link_account -> {
                    openActivity(LinkAccountActivity::class.java)
                }
                R.id.nav_notification -> {
                    openActivity(NotificationActivity::class.java)
                }
                R.id.nav_share -> {
                    Toast.makeText(this, "share with friend", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_rating -> {
                    Toast.makeText(this, "Rating app", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_suggestion -> {
                    openActivity(SuggestionActivity::class.java)
                }
                R.id.nav_app_info -> {
                    openActivity(AppInfoActivity::class.java)
                }
                R.id.nav_password -> {
                    openActivity(SetPasswordActivity::class.java)
                }
                R.id.nav_logout -> {
                    val sharedPref = getSharedPreferences("Auth", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        remove("username")
                        remove("password")
                        apply()
                    }
                    openActivity(LoginActivity::class.java)
                }
            }
            binding.main.closeDrawers()
            true
        }
    }

    private fun openActivity(activityClass: Class<out Activity>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }


    private fun setupToolbar() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.root,
            binding.mainToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.main.addDrawerListener(toggle)
        toggle.syncState()

        switchFragment(SaleFragment(), "Bán hàng", R.id.nav_sale)
    }


    private fun switchFragment(fragment: Fragment, title: String, idItem: Int) {
        val menu = binding.navigationView.menu

        val item = menu.findItem(idItem)
        for (i in 0 until menu.size) {
            menu[i].isChecked = false
        }

        item.isChecked = true

        binding.mainToolbarTitle.text = title
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}