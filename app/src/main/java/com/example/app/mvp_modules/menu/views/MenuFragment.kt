package com.example.app.mvp_modules.menu.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.FragmentMenuBinding
import com.example.app.datas.CukcukDbHelper
import com.example.app.datas.repositories.InventoryRepository
import com.example.app.entities.Inventory
import com.example.app.mvp_modules.menu.adapters.ListInventoryAdapter
import com.example.app.mvp_modules.menu.contracts.MenuContract
import com.example.app.mvp_modules.menu.presenters.MenuPresenter
import java.util.UUID

class MenuFragment : Fragment(), MenuContract.View {
    private lateinit var binding: FragmentMenuBinding
    private lateinit var presenter: MenuContract.Presenter
    private var inventories: MutableList<Inventory> = mutableListOf()
    private lateinit var adapter: ListInventoryAdapter
    private lateinit var formLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        val dbHelper = CukcukDbHelper(requireContext())
        val repository = InventoryRepository(dbHelper)
        presenter = MenuPresenter(this, repository)

        setupToolbar()
        fetchData()
        setupFormLauncher()


        return binding.root
    }

    private fun setupFormLauncher() {
        formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                fetchData()
            }
        }
    }

    private fun setupAdapter() {
        adapter = ListInventoryAdapter(requireContext(), inventories).apply {
            onItemSelected = { inventory ->
                presenter.openInventoryForm(inventory.InventoryID)
            }
        }
        binding.recyclerListDish.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerListDish.adapter = adapter
    }

    override fun showDataInventories(data: MutableList<Inventory>) {
        inventories = data
        setupAdapter()
    }

    private fun fetchData() {
        presenter.fetchData()
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
                        presenter.openInventoryForm(null)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun navigateToInventoryForm(inventoryId: UUID?) {
        val intent = Intent(requireContext(), InventoryFormActivity::class.java)
        intent.putExtra("inventory_data", inventoryId)
        formLauncher.launch(intent)
    }
}
