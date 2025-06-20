package com.example.app.mvp_modules.sale.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.ActivityInvoiceBinding
import com.example.app.datas.CukcukDbHelper
import com.example.app.datas.repositories.InvoiceRepository
import com.example.app.entities.Invoice
import com.example.app.mvp_modules.calculator.CalculatorDialogInvoiceActivityFragment
import com.example.app.mvp_modules.sale.adapters.ListInvoiceDetailBillAdapter
import com.example.app.mvp_modules.sale.contracts.InvoiceContract
import com.example.app.mvp_modules.sale.presenters.InvoicePresenter
import com.example.app.utils.FormatDisplay
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class InvoiceActivity : AppCompatActivity(), InvoiceContract.View {
    private lateinit var binding: ActivityInvoiceBinding
    private lateinit var presenter: InvoiceContract.Presenter
    private lateinit var invoice : Invoice
    private lateinit var adapter: ListInvoiceDetailBillAdapter
    private var fromSaleFragment = false

    private val invoiceFormLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val shouldReload = result.data?.getBooleanExtra("create_new_order", false) == true
            if (shouldReload) {
                val resultIntent = Intent()
                resultIntent.putExtra("create_new_order", true)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = CukcukDbHelper(this)
        val repository = InvoiceRepository(db)
        presenter = InvoicePresenter(this, repository)

        setupToolbar()

        lifecycleScope.launch {
            getInvoiceData()
        }

        binding.btnOpenCalculator.setOnClickListener {
            openCalculator()
        }

        binding.btnSubmitBill.setOnClickListener {
            lifecycleScope.launch {
                paymentInvoice()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun openCalculator() {
        CalculatorDialogInvoiceActivityFragment.newInstance(
            invoice.ReceiveAmount.toString(),
            "Thu tiền",
            "Số tiền",
            0.0,
            999999999.0) { result ->
            invoice.ReceiveAmount = result.toDouble()
            invoice.ReturnAmount = invoice.ReceiveAmount - invoice.Amount
            binding.txtMoneyGive.text = FormatDisplay.formatNumber(result.toString())
            binding.txtReturnMoney.text = FormatDisplay.formatNumber(invoice.ReturnAmount.toString())
        }.show(supportFragmentManager, null)
    }

    override fun navigateInvoiceForm(invoice: Invoice?) {
        if (fromSaleFragment) {
            val intent = Intent(this, InvoiceFormActivity::class.java)
            intent.putExtra("invoice_data", invoice)
            invoiceFormLauncher.launch(intent)
        }
        else
        {
            val resultIntent = Intent()
            resultIntent.putExtra("create_new_order", true)
            setResult(RESULT_OK, resultIntent)
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
        menuInflater.inflate(R.menu.menu_invoice, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.btnSubmitBillToolbar -> {
                lifecycleScope.launch {
                    paymentInvoice()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private suspend fun paymentInvoice() {
        val result = presenter.handlePaymentInvoice(invoice)
        if (result.isSuccess) {
            presenter.createInvoice()
        } else {
            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun getInvoiceData() {
        invoice = intent.getSerializableExtra("invoice_data") as Invoice
        fromSaleFragment = intent.getSerializableExtra("from_sale_fragment") as Boolean

        invoice.InvoiceDetails = presenter.getInvoiceDetails(invoice)
        invoice.InvoiceNo = presenter.getNewInvoiceNo()
        showDataInvoice()
    }

    @SuppressLint("SetTextI18n")
    private fun showDataInvoice() {
        binding.txtTotalPrice.text = FormatDisplay.formatNumber(invoice.Amount.toString())
        binding.txtMoneyGive.text = FormatDisplay.formatNumber(invoice.ReceiveAmount.toString())
        binding.txtReturnMoney.text = FormatDisplay.formatNumber(invoice.ReturnAmount.toString())
        binding.txtCreatedAt.text =  FormatDisplay.formatTo12HourWithCustomAMPM(invoice.InvoiceDate.toString())
        binding.txtBillNumber.text = "Số: ${invoice.InvoiceNo}"

        if (invoice.TableName.isEmpty()) {
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