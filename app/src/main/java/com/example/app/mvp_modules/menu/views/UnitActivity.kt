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
import com.example.app.databinding.ActivityUnitBinding
import com.example.app.datas.CukcukDbHelper
import com.example.app.datas.repositories.UnitRepository
import com.example.app.entities.SeverResponse
import com.example.app.entities.Unit
import com.example.app.mvp_modules.menu.adapters.ListUnitAdapter
import com.example.app.mvp_modules.menu.contracts.UnitContract
import com.example.app.mvp_modules.menu.models.UnitModel
import com.example.app.mvp_modules.menu.presenters.UnitPresenter
import java.time.LocalDateTime
import java.util.UUID

@SuppressLint("NewApi")
class UnitActivity : AppCompatActivity(), UnitContract.View {
    private lateinit var binding: ActivityUnitBinding

    private var unitSelected: Unit = Unit(
        UnitID = null,
        UnitName = "",
        Description = "",
        Inactive = true,
        CreatedDate = LocalDateTime.now(),
        CreatedBy = "",
        ModifiedDate = LocalDateTime.now(),
        ModifiedBy = ""
    )

    private lateinit var presenter : UnitContract.Presenter
    private var units: MutableList<Unit> = mutableListOf()

    private var unitEdited: Unit = Unit(
        UnitID = null,
        UnitName = "",
        Description = "",
        Inactive = true,
        CreatedDate = LocalDateTime.now(),
        CreatedBy = "",
        ModifiedDate = LocalDateTime.now(),
        ModifiedBy = ""
    )

    private lateinit var dialog: AlertDialog
    private lateinit var adapter: ListUnitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUnitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = CukcukDbHelper(this)
        val repository = UnitRepository(dbHelper)
        val model = UnitModel(repository)
        presenter = UnitPresenter(this, model)
        units = presenter.getListUnit()
        setupToolbar()
        getUnitSelected()
        setupAdapter()

        binding.btnSubmitUnit.setOnClickListener {
            presenter.handleUpdateUnitInventory(unitSelected)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupAdapter() {
        adapter = ListUnitAdapter(this, units, unitSelected).apply {
            onUnitSelected = { unitDish ->
                unitSelected = unitDish
            }

            onClickEditButton = { unitDish ->
                unitEdited = unitDish
                openFormPopup(isAddNew = false)
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
                unitEdited = Unit(
                    UnitID = null,
                    UnitName = "",
                    Description = "",
                    Inactive = true,
                    CreatedDate = LocalDateTime.now(),
                    CreatedBy = "",
                    ModifiedDate = LocalDateTime.now(),
                    ModifiedBy = ""
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun openFormPopup(isAddNew: Boolean) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_unit, null)
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
            edtUnitName.setText(unitEdited.UnitName)
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSubmit.setOnClickListener {
            val value = edtUnitName.text.toString()
            unitEdited.UnitName = value
            presenter.handleSubmit(unitEdited, isAddNew)
        }

        dialog.show()
    }


    private fun getUnitSelected() {
        val unitId = intent.getSerializableExtra("unit_id_data") as? UUID
        val unitName = intent.getSerializableExtra("unit_name_data") as? String
        unitSelected.UnitID = unitId
        unitSelected.UnitName = unitName?:""
    }

    private fun updateSelectedData(){
        for (unit in units) {
            if (unit.UnitID == unitSelected.UnitID) {
                unitSelected.UnitName = unit.UnitName
            }
        }
    }

    override fun onClose() {
        finish()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSubmit(response: SeverResponse, isAddNew: Boolean) {
        if (response.isSuccess) {
            dialog.dismiss()
            units = presenter.getListUnit()
            adapter.updateData(units)

            if (isAddNew) return
            updateSelectedData()
        } else {
            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onChangeUnitInventory() {
        val resultIntent = Intent().apply {
            putExtra("unit_data", unitSelected)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}