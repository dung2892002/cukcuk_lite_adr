package com.example.app.mvp_modules.menu.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.ActivityDishUnitBinding
import com.example.app.models.Dish
import com.example.app.models.UnitDish
import com.example.app.mvp_modules.menu.adapters.ListUnitDishAdapter
import com.example.app.mvp_modules.menu.contracts.DishUnitContract
import com.example.app.mvp_modules.menu.presenters.DishUnitPresenter

class DishUnitActivity : AppCompatActivity(), DishUnitContract.View {
    private lateinit var binding: ActivityDishUnitBinding
    private var unitSelected: UnitDish = UnitDish("")
    private lateinit var presenter : DishUnitContract.Presenter
    private var units: MutableList<UnitDish> = mutableListOf()
    private var unitEdited: UnitDish = UnitDish("")
    private lateinit var dialog: AlertDialog
    private lateinit var adapter: ListUnitDishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDishUnitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = DishUnitPresenter(this)
        addDataTest()
        setupToolbar()
        getUnitSelected()
        setupAdapter()

        binding.btnSubmitUnit.setOnClickListener {
            presenter.handleSubmit(unitSelected)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupAdapter() {
        adapter = ListUnitDishAdapter(this, units, unitSelected).apply {
            onUnitSelected = { unitDish ->
                unitSelected = unitDish
            }

            onClickEditButton = { unitDish ->
                unitEdited = unitDish
                openFormPopup(false)
            }
        }
        binding.recyclerListUnitDish.layoutManager = LinearLayoutManager(this)
        binding.recyclerListUnitDish.adapter = adapter
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dish_unit_activity, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.btnOpenForm -> {
                openFormPopup(true)
                unitEdited = UnitDish("")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun openFormPopup(isAddNew: Boolean) {
        println(isAddNew)
        println(unitEdited.name)

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_unit_dish, null)
        val txtLabelDialogUnitDish = dialogView.findViewById<TextView>(R.id.txtLabelDialogUnitDish)
        val edtUnitName = dialogView.findViewById<EditText>(R.id.edtUnitName)
        val btnClose = dialogView.findViewById<ImageButton>(R.id.btnCloseDialogDishUnit)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnFooterCloseDishUnitDialog)
        val btnSubmit = dialogView.findViewById<Button>(R.id.btnSubmitUnitDishDialog)

        dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        if (isAddNew) {
            txtLabelDialogUnitDish.text = "Thêm đơn vị tính"
        } else {
            txtLabelDialogUnitDish.text = "Sửa đơn vị tính"
            edtUnitName.setText(unitEdited.name)
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSubmit.setOnClickListener {
            val value = edtUnitName.text.toString()
            unitEdited.name = value
            if (isAddNew) {
                units.add(unitEdited)
                adapter.notifyDataSetChanged()
            } else {
                adapter.notifyDataSetChanged()
            }
            dialog.dismiss()
        }

        dialog.show()
    }


    //sau thay bang lay data tu api
    private fun addDataTest() {
        units.add(UnitDish("Cái"))
        units.add(UnitDish("Chai"))
        units.add(UnitDish("Chén"))
        units.add(UnitDish("Cốc"))
        units.add(UnitDish("Lon"))
        units.add(UnitDish("Suất"))
    }

    private fun getUnitSelected() {
        val unit = intent.getSerializableExtra("unit_data") as? UnitDish
        unitSelected = unit!!
    }


    override fun onClose() {
        finish()
    }

    override fun onSubmit() {
        val resultIntent = Intent().apply {
            putExtra("unit_data", unitSelected)
        }
        println(unitSelected.name)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}