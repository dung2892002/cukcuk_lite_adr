package com.example.app.mvp_modules.sale.views

import android.annotation.SuppressLint
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
import com.example.app.databinding.ActivitySelectInventoryBinding
import com.example.app.datas.CukcukDbHelper
import com.example.app.datas.repositories.InvoiceRepository
import com.example.app.dto.InventorySelect
import com.example.app.entities.Invoice
import com.example.app.dto.SeverResponse
import com.example.app.mvp_modules.calculator.CalculatorDialogFragment
import com.example.app.mvp_modules.calculator.CalculatorDialogOrderFragment
import com.example.app.mvp_modules.sale.adapters.ListSelectInventoryAdapter
import com.example.app.mvp_modules.sale.contracts.InvoiceFormContract
import com.example.app.mvp_modules.sale.presenters.InvoiceFormPresenter
import com.example.app.utils.FormatDisplay
import java.time.LocalDateTime

class InvoiceFormActivity : AppCompatActivity(), InvoiceFormContract.View {
    private lateinit var binding: ActivitySelectInventoryBinding
    private lateinit var presenter: InvoiceFormContract.Presenter
    private lateinit var invoice: Invoice
    private var inventoriesSelect = mutableListOf<InventorySelect>()
    private lateinit var adapter: ListSelectInventoryAdapter
    private var positionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySelectInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = CukcukDbHelper(this)
        val repository = InvoiceRepository(db)
        presenter = InvoiceFormPresenter(this, repository)

        setupToolbar()
        getDataOrder()

        binding.btnCreateBill.setOnClickListener {
            paymentInvoice()
        }

        binding.btnSaveOrder.setOnClickListener {
            presenter.submitInvoice(invoice, inventoriesSelect, false)
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
        val initValue = if (!invoice.TableName.isEmpty()) invoice.TableName else "0"
        CalculatorDialogOrderFragment.newInstance(initValue, "Nhập số bàn") { result ->
            invoice.TableName = FormatDisplay.formatNumber(result.toString())
            binding.btnOpenInputTable.text = FormatDisplay.formatNumber(result.toString())
        }.show(supportFragmentManager, null)
    }

    override fun openCalculatorPeople() {
        val initValue = if (invoice.NumberOfPeople != 0) invoice.NumberOfPeople.toString() else "0"
        CalculatorDialogOrderFragment.newInstance(initValue, "Nhập số người") { result ->
            invoice.NumberOfPeople = result.toInt()
            binding.btnOpenInputPeople.text = FormatDisplay.formatNumber(result.toString())
        }.show(supportFragmentManager, null)
    }

    override fun openCalculatorDish() {
        val initValue = inventoriesSelect[positionIndex].quantity
        CalculatorDialogFragment.newInstance(initValue.toString(), "Nhập số lượng") { result ->
            inventoriesSelect[positionIndex].quantity = result
            presenter.calculatorTotalPrice(inventoriesSelect)
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
        if (requestCode == 500 && resultCode == RESULT_OK) {
            val createNewOrder = data?.getBooleanExtra("create_new_order", false) == true
            if (createNewOrder) {
                finish() // Kết thúc activity hiện tại
                startActivity(Intent(this, InvoiceFormActivity::class.java)) // Mở activity mới
            }
        }
    }

    private fun paymentInvoice() {
        if (invoice.Amount == 0.0) {
            Toast.makeText(this, "Vui lòng chọn món", Toast.LENGTH_SHORT).show()
            return
        }
        presenter.paymentInvoice(inventoriesSelect, invoice)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_select_dish, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navigateToSale()
                true
            }

            R.id.btnSubmitSelectDishToolbar -> {
                presenter.paymentInvoice(inventoriesSelect, invoice)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun updateTotalPrice(newPrice: Double) {
        invoice.Amount = newPrice
        binding.txtOrderTotalPrice.text = FormatDisplay.formatNumber(newPrice.toString())
    }

    @SuppressLint("NewApi")
    private fun getDataOrder() {
        val invoiceData = intent.getSerializableExtra("invoice_data") as Invoice?
        invoice = invoiceData
            ?: Invoice(
                InvoiceID = null,
                InvoiceType = 1,
                InvoiceNo = "",
                InvoiceDate = LocalDateTime.now(),
                Amount = 0.0,
                ReceiveAmount = 0.0,
                ReturnAmount = 0.0,
                RemainAmount = 0.0,
                JournalMemo = "",
                PaymentStatus = 0,
                NumberOfPeople = 0,
                TableName = "",
                ListItemName = "",
                CreatedDate = LocalDateTime.now(),
                CreatedBy = "",
                ModifiedDate = LocalDateTime.now(),
                ModifiedBy = "",
                InvoiceDetails = mutableListOf()
            )

        binding.btnOpenInputTable.text = if (!invoice.TableName.isEmpty()) invoice.TableName else ""
        binding.btnOpenInputPeople.text = if (invoice.NumberOfPeople != 0) invoice.NumberOfPeople.toString() else ""
        binding.txtOrderTotalPrice.text = FormatDisplay.formatNumber(invoice.Amount.toString())

        inventoriesSelect = presenter.handleDataInventorySelect(invoice)
        setupAdapter()
    }

    override fun navigateToInvoiceActivity(invoice: Invoice) {
        val intent = Intent(this, InvoiceActivity::class.java)
        intent.putExtra("invoice_data", invoice)
        intent.putExtra("from_sale_fragment", false)
        startActivityForResult(intent, 500)
    }


    override fun closeActivity(response: SeverResponse) {
        if (response.isSuccess) {
            navigateToSale()
        } else {
            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToSale() {
        val resultIntent = Intent()
        resultIntent.putExtra("create_new_order", true)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun setupAdapter() {
        adapter = ListSelectInventoryAdapter(this,inventoriesSelect).apply {
            onClickItem = { dishSelect, position ->
                inventoriesSelect[position].quantity++
                presenter.calculatorTotalPrice(inventoriesSelect)
            }

            onClickButtonAdd = { dishSelect, position ->
                inventoriesSelect[position].quantity++
                presenter.calculatorTotalPrice(inventoriesSelect)
            }

            onClickButtonSubtract = { dishSelect, position ->
                if (inventoriesSelect[position].quantity > 0) {
                    inventoriesSelect[position].quantity--
                    presenter.calculatorTotalPrice(inventoriesSelect)
                }
            }

            onClickButtonRemove = { dishSelect, position ->
                inventoriesSelect[position].quantity = 0.0
                presenter.calculatorTotalPrice(inventoriesSelect)
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