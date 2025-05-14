package com.example.app.mvp_modules.sale.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.ActivityBillBinding
import com.example.app.models.Bill
import com.example.app.models.Order
import com.example.app.models.UnitDish
import com.example.app.mvp_modules.calculator.CalculatorDialogFragment
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
    private var fromSaleFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = BillPresenter(this)

        setupToolbar()
        getBillData()

        binding.btnOpenCalculator.setOnClickListener {
            openCalculator()
        }

        binding.btnSubmitBill.setOnClickListener {
            createBill()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun openCalculator() {
        CalculatorDialogFragment.newInstance(bill.moneyGive.toString()) {result ->
            bill.moneyGive = result.toDouble()
            bill.moneyReturn = bill.moneyGive - bill.order.totalPrice
            binding.txtMoneyGive.text = FormatDisplay.formatNumber(result.toString())
            binding.txtReturnMoney.text = FormatDisplay.formatNumber(bill.moneyReturn.toString())
        }.show(supportFragmentManager, null)

    }

    override fun navigateCreateOrder(order: Order?) {
        if (fromSaleFragment) {
            val intent = Intent(this, SelectDishActivity::class.java)
            intent.putExtra("order_data", order)
            startActivity(intent)
            finish()
        }
        else
        {
            val resultIntent = Intent()
            resultIntent.putExtra("order_data", order) // đơn hàng mới
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
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

            R.id.btnSubmitBillToolbar -> {
                createBill()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createBill() {
        val result = presenter.handleCreateBill(bill)
        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
        if (result.isSuccess) {
            presenter.createOrder()
        }
    }

    private fun getBillData() {
        bill = intent.getSerializableExtra("bill_data") as Bill
        fromSaleFragment = intent.getSerializableExtra("from_sale_fragment") as Boolean
        println(bill.order.dishes.size)
        showDataBill()
    }

    @SuppressLint("SetTextI18n")
    private fun showDataBill() {
        binding.txtTotalPrice.text = FormatDisplay.formatNumber(bill.order.totalPrice.toString())
        binding.txtMoneyGive.text = FormatDisplay.formatNumber(bill.moneyGive.toString())
        binding.txtReturnMoney.text = FormatDisplay.formatNumber(bill.moneyReturn.toString())
        binding.txtCreatedAt.text = "Ngày: ${FormatDisplay.formatTo12HourWithCustomAMPM(bill.createdAt)}"
        binding.txtBillNumber.text = "Số: ${bill.id}"

        if (bill.order.tableNumber == null) {
            binding.txtNumberTable.visibility = View.GONE
        } else {
            binding.txtNumberTable.text = "Bàn: ${bill.order.tableNumber}"
            binding.txtNumberTable.visibility = View.VISIBLE
        }

        adapter = ListDishOrderBillAdapter(this, bill.order.dishes)
        binding.recyclerListDishOrderInBill.layoutManager = LinearLayoutManager(this)
        binding.recyclerListDishOrderInBill.adapter = adapter
    }
}