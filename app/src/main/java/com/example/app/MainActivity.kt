package com.example.app
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.databinding.ActivityMainBinding
import com.example.app.mvp_modules.menu.views.MenuFragment
import com.example.app.mvp_modules.sale.views.SaleFragment
import com.example.app.mvp_modules.statistic.views.StatisticFragment
import androidx.core.view.size
import androidx.core.view.get
import com.example.app.databinding.NavHeaderBinding
import com.example.app.mvp_modules.login.LoginActivity
import com.example.app.utils.LocaleHelper

class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var binding: ActivityMainBinding
    private lateinit var headerBinding: NavHeaderBinding
    private lateinit var presenter: MainContract.Presenter

    override fun attachBaseContext(newBase: Context) {
        val context = LocaleHelper.setLocale(newBase, "vi")
        super.attachBaseContext(context)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = MainPresenter(this)

        if (!presenter.checkLogin(this)) {
            openLoginScreen()
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


    override fun openActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    override fun switchFragment(title: String) {
        val fragment = when (title) {
            "Bán hàng" -> SaleFragment()
            "Thực đơn" -> MenuFragment()
            "Doanh thu" -> StatisticFragment()
            else -> null
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, it)
                .commit()
            binding.mainToolbarTitle.text = title
        }
    }

    override fun openLoginScreen() {
        openActivity(LoginActivity::class.java)
    }

    override fun showUserInfo(fullName: String, username: String) {
        headerBinding.txtNavHeaderFullName.text = fullName
        headerBinding.txtNavHeaderUsername.text = username
    }

    override fun closeDrawer() {
        binding.main.closeDrawers()
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
            presenter.handleNavigationItemSelected(menuItem.itemId)
            true
        }
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

        switchFragment("Bán hàng")
    }

}