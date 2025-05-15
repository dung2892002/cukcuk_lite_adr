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
import com.example.app.databinding.ActivityInvoiceBinding
import com.example.app.entities.Invoice
import com.example.app.mvp_modules.calculator.CalculatorDialogBillActivityFragment
import com.example.app.mvp_modules.sale.adapters.ListInvoiceDetailBillAdapter
import com.example.app.mvp_modules.sale.contracts.InventoryContract
import com.example.app.mvp_modules.sale.presenters.InvoicePresenter
import com.example.app.utils.FormatDisplay

class InvoiceActivity : AppCompatActivity(), InventoryContract.View {
    private lateinit var binding: ActivityInvoiceBinding
    private lateinit var presenter: InventoryContract.Presenter
    private lateinit var invoice : Invoice
    private lateinit var adapter: ListInvoiceDetailBillAdapter
    private var fromSaleFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = InvoicePresenter(this)

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
        CalculatorDialogBillActivityFragment.newInstance(invoice.ReceiveAmount.toString()) { result ->
            invoice.ReceiveAmount = result.toDouble()
            invoice.ReturnAmount = invoice.ReceiveAmount - invoice.Amount
            binding.txtMoneyGive.text = FormatDisplay.formatNumber(result.toString())
            binding.txtReturnMoney.text = FormatDisplay.formatNumber(invoice.ReturnAmount.toString())
        }.show(supportFragmentManager, null)
    }

    override fun navigateCreateOrder(invoice: Invoice?) {
        if (fromSaleFragment) {
            val intent = Intent(this, SelectInventoryActivity::class.java)
            intent.putExtra("order_data", invoice)
            startActivity(intent)
            finish()
        }
        else
        {
            val resultIntent = Intent()
            resultIntent.putExtra("create_new_order", true)
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
        val result = presenter.handlePaymentInvoice(invoice)
        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
        if (result.isSuccess) {
            presenter.createOrder()
        }
    }

    private fun getBillData() {
        invoice = intent.getSerializableExtra("invoice_data") as Invoice
        fromSaleFragment = intent.getSerializableExtra("from_sale_fragment") as Boolean
        println(invoice.InvoiceDetails.size)
        showDataBill()
    }

    @SuppressLint("SetTextI18n")
    private fun showDataBill() {
        binding.txtTotalPrice.text = FormatDisplay.formatNumber(invoice.Amount.toString())
        binding.txtMoneyGive.text = FormatDisplay.formatNumber(invoice.ReceiveAmount.toString())
        binding.txtReturnMoney.text = FormatDisplay.formatNumber(invoice.ReturnAmount.toString())
        binding.txtCreatedAt.text =  FormatDisplay.formatTo12HourWithCustomAMPM(invoice.CreatedDate.toString())
        binding.txtBillNumber.text = "Số: ${invoice.InvoiceNo}"

        if (!invoice.TableName.isEmpty()) {
            binding.groupNumberTable.visibility = View.GONE
        } else {
            binding.groupNumberTable.visibility = View.VISIBLE
            binding.txtNumberTable.text = invoice.TableName
            binding.txtNumberTable.visibility = View.VISIBLE
        }

        adapter = ListInvoiceDetailBillAdapter(this, invoice.InvoiceDetails)
        binding.recyclerListDishOrderInBill.layoutManager = LinearLayoutManager(this)
        binding.recyclerListDishOrderInBill.adapter = adapter
    }
}