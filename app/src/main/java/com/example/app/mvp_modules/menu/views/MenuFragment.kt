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
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.FragmentMenuBinding
import com.example.app.models.Dish
import com.example.app.models.UnitDish
import com.example.app.mvp_modules.menu.adapters.ListDishAdapter
import com.example.app.mvp_modules.menu.contracts.MenuContract
import com.example.app.mvp_modules.menu.presenters.MenuPresenter
import java.util.UUID

class MenuFragment : Fragment(), MenuContract.View {
    private lateinit var binding: FragmentMenuBinding
    private lateinit var presenter: MenuContract.Presenter
    private var dishes: MutableList<Dish> = mutableListOf()
    private lateinit var adapter: ListDishAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        presenter = MenuPresenter(this)

        setupToolbar()
        fetchData()


        return binding.root
    }

    private fun setupAdapter() {
        adapter = ListDishAdapter(requireContext(), dishes).apply {
            onItemSelected = { dish ->
                presenter.openDishForm(dish)
            }
        }
        binding.recyclerListDish.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerListDish.adapter = adapter
    }

    override fun showDataDishes(data: MutableList<Dish>) {
        dishes = data
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
                        presenter.openDishForm(null)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun navigateToDishForm(dish: Dish?) {
        val intent = Intent(requireContext(), DishFormActivity::class.java)
        intent.putExtra("dish_data", dish)
        startActivity(intent)
    }
}
