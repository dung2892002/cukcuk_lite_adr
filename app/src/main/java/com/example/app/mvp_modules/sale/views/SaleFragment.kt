package com.example.app.mvp_modules.sale.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.FragmentSaleBinding
import com.example.app.datas.CukcukDbHelper
import com.example.app.datas.repositories.InvoiceRepository
import com.example.app.entities.Invoice
import com.example.app.mvp_modules.sale.presenters.SalePresenter
import com.example.app.mvp_modules.sale.adapters.ListInvoiceAdapter
import com.example.app.mvp_modules.sale.contracts.SaleContract
import kotlinx.coroutines.launch

class SaleFragment : Fragment(), SaleContract.View {
    private lateinit var binding: FragmentSaleBinding
    private lateinit var presenter: SaleContract.Presenter
    private var invoices = mutableListOf<Invoice>()
    private lateinit var adapter: ListInvoiceAdapter
    private lateinit var dialog: AlertDialog

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch {
            invoices = presenter.fetchData()
            showDataInvoices(invoices)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaleBinding.inflate(inflater, container, false)

        val db = CukcukDbHelper(requireContext())
        val repository = InvoiceRepository(db)
        presenter = SalePresenter(this, repository)

        setupToolbar()
        viewLifecycleOwner.lifecycleScope.launch {
            invoices = presenter.fetchData()
            showDataInvoices(invoices)
        }

        binding.txtButtonAddInvoice.setOnClickListener {
            presenter.handleNavigateInvoiceForm(null)
        }

        return binding.root
    }

    private fun setupToolbar() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main_toolbar, menu)
                menu.findItem(R.id.action_custom)?.isVisible = true
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_custom -> {
                        presenter.handleNavigateInvoiceForm(null)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun setupAdapter() {
        adapter = ListInvoiceAdapter(requireContext(), invoices).apply {
            onItemClick = { order ->
                presenter.handleNavigateInvoiceForm(order)
            }

            onClickButtonDelete = { order ->
                openDialogDelete(order)
            }
            onClickButtonCreateBill = {order ->
                presenter.paymentInvoice(order)
            }
        }
        binding.recyclerListOrder.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerListOrder.adapter = adapter
    }

    private fun openDialogDelete(invoice: Invoice) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm_delete, null)
        val content = dialogView.findViewById<TextView>(R.id.content)
        val btnClose = dialogView.findViewById<ImageButton>(R.id.btnCloseDeleteDialog)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancelDelete)
        val btnSubmit = dialogView.findViewById<Button>(R.id.btnAcceptDelete)
        dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val message = "Bạn có chắc muốn hủy các món đã chọn không?"

        content.text = message

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSubmit.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val result = presenter.handleDeleteInvoice(invoice)
                if (result.isSuccess) {
                    invoices = presenter.fetchData()
                    adapter.updateAdapter(invoices)
                    if (invoices.isEmpty()) {
                        binding.fragmentStateEmptyOrder.visibility = View.VISIBLE
                        binding.recyclerListOrder.visibility = View.GONE
                    } else {
                        binding.fragmentStateEmptyOrder.visibility = View.GONE
                        binding.recyclerListOrder.visibility = View.VISIBLE
                    }
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }

    override fun showDataInvoices(data: MutableList<Invoice>) {
        if (data.isEmpty()) {
            binding.fragmentStateEmptyOrder.visibility = View.VISIBLE
            binding.recyclerListOrder.visibility = View.GONE
        }
        else {
            binding.fragmentStateEmptyOrder.visibility = View.GONE
            binding.recyclerListOrder.visibility = View.VISIBLE
            invoices = data
            setupAdapter()
        }
    }

    override fun navigateToInvoiceActivity(invoice: Invoice) {
        val intent = Intent(requireContext(), InvoiceActivity::class.java)
        intent.putExtra("invoice_data", invoice)
        intent.putExtra("from_sale_fragment", true)
        startActivity(intent)
    }

    override fun navigateToInvoiceFormActivity(invoice: Invoice?) {
        val intent = Intent(requireContext(), InvoiceFormActivity::class.java)
        intent.putExtra("invoice_data", invoice)
        startActivity(intent)
    }
}