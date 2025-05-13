package com.example.app.mvp_modules.sale.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.FragmentSaleBinding
import com.example.app.models.Bill
import com.example.app.models.Order
import com.example.app.mvp_modules.sale.presenters.SalePresenter
import com.example.app.mvp_modules.sale.adapters.ListOrderAdapter
import com.example.app.mvp_modules.sale.contracts.SaleContract

class SaleFragment : Fragment(), SaleContract.View {
    private lateinit var binding: FragmentSaleBinding
    private lateinit var presenter: SaleContract.Presenter
    private var orders = mutableListOf<Order>()
    private lateinit var adapter: ListOrderAdapter
    private lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentSaleBinding.inflate(inflater, container, false)
        presenter = SalePresenter(this)

        setupToolbar()
        fetchData()


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
                        presenter.handleNavigateSelectDish(null)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun fetchData() {
        presenter.fetchData()
    }

    private fun setupAdapter() {
        adapter = ListOrderAdapter(requireContext(), orders).apply {
            onItemClick = { order ->
                presenter.handleNavigateSelectDish(order)
            }

            onClickButtonDelete = { order ->
                openDialogDelete(order)
            }
            onClickButtonCreateBill = {order ->
                presenter.createBill(order)
            }
        }
        binding.recyclerListOrder.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerListOrder.adapter = adapter
    }

    private fun openDialogDelete(order: Order) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete_order, null)
        dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<ImageButton>(R.id.btnExitDeleteOrderDialog).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<AppCompatButton>(R.id.btnCancelDeleteOrder).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<AppCompatButton>(R.id.btnAcceptDeleteOrder).setOnClickListener {
            val result = presenter.deleteOrder(order)
            Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
            if (result.isSuccess) {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    override fun showDataOrders(data: MutableList<Order>) {
        if (data.isEmpty()) {
            binding.fragmentStateEmptyOrder.visibility = View.VISIBLE
            binding.recyclerListOrder.visibility = View.GONE
        }
        else {
            binding.fragmentStateEmptyOrder.visibility = View.GONE
            binding.recyclerListOrder.visibility = View.VISIBLE
            orders = data
            setupAdapter()
        }
    }

    override fun navigateToBillActivity(bill: Bill) {
        println(bill.order.dishes.size)
        val intent = Intent(requireContext(), BillActivity::class.java)
        intent.putExtra("bill_data", bill)
        startActivity(intent)
    }

    override fun navigateToSelectDishActivity(order: Order?) {
        if (order == null) println("them moi")
        else println("update")
    }
}