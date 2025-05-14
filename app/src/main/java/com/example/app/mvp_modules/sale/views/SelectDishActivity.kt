package com.example.app.mvp_modules.sale.views

import android.app.Activity
import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.ActivitySelectDishBinding
import com.example.app.models.Bill
import com.example.app.models.DishSelect
import com.example.app.models.Order
import com.example.app.models.OrderDish
import com.example.app.models.SeverResponse
import com.example.app.mvp_modules.calculator.CalculatorDialogFragment
import com.example.app.mvp_modules.calculator.CalculatorDialogOrderFragment
import com.example.app.mvp_modules.sale.adapters.ListSelectDishAdapter
import com.example.app.mvp_modules.sale.contracts.SelectDishContract
import com.example.app.mvp_modules.sale.presenters.SelectDishPresenter
import com.example.app.utils.FormatDisplay

class SelectDishActivity : AppCompatActivity(), SelectDishContract.View {
    private lateinit var binding: ActivitySelectDishBinding
    private lateinit var presenter: SelectDishContract.Presenter
    private lateinit var order: Order
    private var dishesSelect = mutableListOf<DishSelect>()
    private lateinit var adapter: ListSelectDishAdapter
    private var positionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySelectDishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = SelectDishPresenter(this)

        setupToolbar()
        getDataOrder()

        binding.btnCreateBill.setOnClickListener {
            createBill()
        }

        binding.btnSaveOrder.setOnClickListener {
            presenter.submitOrder(order)
        }

        binding.btnOpenInputTable.setOnClickListener {
            openCalculatorTable()
        }

        binding.btnOpenInputPeople.setOnClickListener {
            openCalculatorPeople()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun openCalculatorTable() {
        val initValue = if (order.tableNumber != null) order.tableNumber.toString() else "0"
        CalculatorDialogOrderFragment.newInstance(initValue, "Nhập số bàn") { result ->
            order.tableNumber = result.toInt()
            binding.btnOpenInputTable.text = FormatDisplay.formatNumber(result.toString())
        }.show(supportFragmentManager, null)
    }

    override fun openCalculatorPeople() {
        val initValue = if (order.quantityPeople != null) order.tableNumber.toString() else "0"
        CalculatorDialogOrderFragment.newInstance(initValue, "Nhập số người") { result ->
            order.quantityPeople = result.toInt()
            binding.btnOpenInputPeople.text = FormatDisplay.formatNumber(result.toString())
        }.show(supportFragmentManager, null)
    }

    override fun openCalculatorDish() {
        val initValue = dishesSelect[positionIndex].quantity
        CalculatorDialogOrderFragment.newInstance(initValue.toString(), "Nhập số lượng") { result ->
            dishesSelect[positionIndex].quantity = result.toInt()
            presenter.calculatorTotalPrice(dishesSelect)
            adapter.notifyItemChanged(positionIndex)
        }.show(supportFragmentManager, null)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        if (requestCode == 500 && resultCode == Activity.RESULT_OK) {
            val createNewOrder = data?.getBooleanExtra("create_new_order", false) == true
            if (createNewOrder) {
                finish() // Kết thúc activity hiện tại
                startActivity(Intent(this, SelectDishActivity::class.java)) // Mở activity mới
            }
        }
    }

    private fun createBill() {
        if (order.totalPrice == 0.0) {
            Toast.makeText(this, "Vui lòng chọn món", Toast.LENGTH_SHORT).show()
            return
        }
        presenter.createBill(dishesSelect, order)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_select_dish, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.btnSubmitSelectDishToolbar -> {
                presenter.createBill(dishesSelect, order)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun updateTotalPrice(newPrice: Double) {
        order.totalPrice = newPrice
        binding.txtOrderTotalPrice.text = FormatDisplay.formatNumber(newPrice.toString())
    }

    private fun getDataOrder() {
        val orderData = intent.getSerializableExtra("order_data") as Order?
        order = orderData
            ?: Order(
                id = null,
                tableNumber = null,
                quantityPeople = null,
                totalPrice = 0.0,
                dishes = mutableListOf<OrderDish>()
            )

        binding.btnOpenInputTable.text = if (order.tableNumber != null) order.tableNumber.toString() else ""
        binding.btnOpenInputPeople.text = if (order.quantityPeople != null) order.quantityPeople.toString() else ""
        binding.txtOrderTotalPrice.text = FormatDisplay.formatNumber(order.totalPrice.toString())

        dishesSelect = presenter.handleDataDishSelect(order)
        setupAdapter()
    }

    override fun navigateToBillActivity(bill: Bill) {
        val intent = Intent(this, BillActivity::class.java)
        intent.putExtra("bill_data", bill)
        intent.putExtra("from_sale_fragment", false)
        startActivityForResult(intent, 500)
    }

    override fun closeActivity(response: SeverResponse) {
        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
        if (response.isSuccess) finish()
    }

    private fun setupAdapter() {
        adapter = ListSelectDishAdapter(this,dishesSelect).apply {
            onClickItem = { dishSelect, position ->
                dishesSelect[position].quantity++
                presenter.calculatorTotalPrice(dishesSelect)
            }

            onClickButtonAdd = { dishSelect, position ->
                dishesSelect[position].quantity++
                presenter.calculatorTotalPrice(dishesSelect)
            }

            onClickButtonSubtract = { dishSelect, position ->
                if (dishesSelect[position].quantity > 0) {
                    dishesSelect[position].quantity--
                    presenter.calculatorTotalPrice(dishesSelect)
                }
            }

            onClickButtonRemove = { dishSelect, position ->
                dishesSelect[position].quantity = 0
                presenter.calculatorTotalPrice(dishesSelect)
            }

            onClickOpenCalculator = { dishSelect, position ->
                positionIndex = position
                openCalculatorDish()
            }
        }

        binding.recyclerListDishSelect.layoutManager = LinearLayoutManager(this)
        binding.recyclerListDishSelect.adapter = adapter
    }
}