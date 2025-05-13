package com.example.app.mvp_modules.sale.views

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.ActivityBillBinding
import com.example.app.models.Bill
import com.example.app.models.UnitDish
import com.example.app.mvp_modules.sale.adapters.ListDishOrderBillAdapter
import com.example.app.mvp_modules.sale.adapters.ListOrderAdapter
import com.example.app.mvp_modules.sale.contracts.BillContract
import com.example.app.mvp_modules.sale.presenters.BillPresenter
import com.example.app.utils.FormatDisplay

class BillActivity : AppCompatActivity(), BillContract.View {
    private lateinit var binding: ActivityBillBinding
    private lateinit var presenter: BillContract.Presenter
    private lateinit var bill : Bill
    private lateinit var adapter: ListDishOrderBillAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = BillPresenter(this)

        setupToolbar()
        getBillData()

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bill, menu);
        return true;
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

    private fun getBillData() {
        bill = intent.getSerializableExtra("bill_data") as Bill
        println(bill.order.dishes.size)
        showDataBill()
    }

    private fun showDataBill() {
        binding.txtTotalPrice.text = FormatDisplay.formatNumber(bill.order.totalPrice.toString())
        binding.txtMoneyGive.text = FormatDisplay.formatNumber(bill.moneyGive.toString())
        binding.txtReturnMoney.text = FormatDisplay.formatNumber(bill.moneyReturn.toString())

        adapter = ListDishOrderBillAdapter(this, bill.order.dishes)
        binding.recyclerListDishOrderInBill.layoutManager = LinearLayoutManager(this)
        binding.recyclerListDishOrderInBill.adapter = adapter
    }
}