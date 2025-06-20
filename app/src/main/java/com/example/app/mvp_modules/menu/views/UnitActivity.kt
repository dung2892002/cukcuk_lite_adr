package com.example.app.mvp_modules.menu.views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.ActivityUnitBinding
import com.example.app.datas.CukcukDbHelper
import com.example.app.datas.repositories.UnitRepository
import com.example.app.dto.SeverResponse
import com.example.app.entities.Unit
import com.example.app.mvp_modules.menu.adapters.ListUnitAdapter
import com.example.app.mvp_modules.menu.contracts.UnitContract
import com.example.app.mvp_modules.menu.presenters.UnitPresenter
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

@Suppress("DEPRECATION")
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
        presenter = UnitPresenter(this, repository)

        lifecycleScope.launch {
            units = presenter.getListUnit()
            getUnitSelected()
            setupAdapter()
        }

        setupToolbar()

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
            onUnitSelected = { unit ->
                unitSelected = unit
            }

            onClickEditButton = { unit ->
                unitEdited = unit
                openFormPopup(isAddNew = false)
            }

            onHoldUnit = { unit, view ->
                showContextMenu()
            }
        }
        binding.recyclerListUnitDish.layoutManager = LinearLayoutManager(this)
        binding.recyclerListUnitDish.adapter = adapter
    }

    private fun showContextMenu() {
        adapter.onHoldUnit = { unit, view ->
            unitEdited = unit

            val contextWrapper = androidx.appcompat.view.ContextThemeWrapper(this, R.style.ContextMenuStyle)
            val popup = PopupMenu(contextWrapper, view)
            popup.menuInflater.inflate(R.menu.menu_unit_context, popup.menu)

            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field.get(popup)
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_delete_unit -> {
                        unitEdited = unit
                        openDialogConfirmDelete(unit)
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun openDialogConfirmDelete(unit: Unit) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null)
        val content = dialogView.findViewById<TextView>(R.id.content)
        val btnClose = dialogView.findViewById<ImageButton>(R.id.btnCloseDeleteDialog)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancelDelete)
        val btnSubmit = dialogView.findViewById<Button>(R.id.btnAcceptDelete)

        dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val unitName = unit.UnitName
        val message = "Bạn có chắc muốn xóa đơn vị tính $unitName không?"

        val spannable = SpannableString(message)
        val start = message.indexOf(unitName)
        val end = start + unitName.length

        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        content.text = spannable


        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSubmit.setOnClickListener {
            lifecycleScope.launch {
                dialog.dismiss()
                presenter.handleDelete(unit)
            }
        }

        dialog.show()
    }


    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_inventory_unit_activity, menu)
        return true
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
            txtLabelDialogUnitDish.text = getString(R.string.label_form_unit_add)
        } else {
            txtLabelDialogUnitDish.text = getString(R.string.label_form_unit_update)
            edtUnitName.setText(unitEdited.UnitName)
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSubmit.setOnClickListener {
            lifecycleScope.launch {
                val value = edtUnitName.text.toString()
                var newUnit = unitEdited.copy()
                newUnit.UnitName = value
                presenter.handleSubmit(newUnit, isAddNew)
            }
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

    override fun onDelete(response: SeverResponse) {
        if (response.isSuccess) {
            lifecycleScope.launch {
                units = presenter.getListUnit()
                adapter.updateData(units)
                if (unitSelected.UnitID == unitEdited.UnitID) {
                    unitSelected.UnitID = null
                    unitSelected.UnitName = ""
                }
            }
        } else {
            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClose() {
        finish()
    }

    @SuppressLint("NotifyDataSetChanged")
    override suspend fun onSubmit(response: SeverResponse, isAddNew: Boolean) {
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
        if (unitSelected.UnitID == null) {
            Toast.makeText(this, "Bạn chưa chọn đơn vị tính", Toast.LENGTH_SHORT).show()
            return
        }
        val resultIntent = Intent().apply {
            putExtra("unit_data", unitSelected)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}