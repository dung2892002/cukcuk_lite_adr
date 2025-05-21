package com.example.app.mvp_modules.menu.views

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.R
import com.example.app.entities.Inventory
import com.example.app.mvp_modules.menu.contracts.InventoryFormContract
import com.example.app.mvp_modules.menu.presenters.InventoryFormPresenter
import java.util.UUID
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.ActivityInventoryFormBinding
import com.example.app.datas.CukcukDbHelper
import com.example.app.datas.repositories.InventoryRepository
import com.example.app.entities.Unit
import com.example.app.mvp_modules.calculator.CalculatorDialogFragment
import com.example.app.mvp_modules.menu.adapters.ListColorAdapter
import com.example.app.mvp_modules.menu.adapters.ListImageAdapter
import com.example.app.utils.FormatDisplay
import com.example.app.utils.ImageHelper
import java.time.LocalDateTime

class InventoryFormActivity : AppCompatActivity(), InventoryFormContract.View {
    private lateinit var binding: ActivityInventoryFormBinding
    private lateinit var presenter: InventoryFormContract.Presenter
    @SuppressLint("NewApi")
    private var inventory: Inventory = Inventory(
        InventoryID = UUID.randomUUID(),
        InventoryCode = "",
        InventoryName = "",
        InventoryType = 0,
        Price = 0.0,
        Description = "",
        Inactive = true,
        CreatedDate = LocalDateTime.now(),
        CreatedBy = "",
        ModifiedDate = LocalDateTime.now(),
        ModifiedBy = "",
        Color = "#00FF00",
        IconFileName = "ic_default.png",
        UseCount = 0,
        UnitID = null,
        UnitName = ""
    )

    private var isAddNew: Boolean = true
    private lateinit var unitDishLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityInventoryFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = CukcukDbHelper(this)
        val repository = InventoryRepository(dbHelper)
        presenter = InventoryFormPresenter(this, repository)

        getInventory()
        setupToolbar()

        binding.btnSubmit.setOnClickListener {
            handleSubmitForm()
        }

        binding.btnSelectColor.setOnClickListener {
            showColorPickerPopup()
        }

        binding.btnSelectImage.setOnClickListener {
            showImagePickerPopup()
        }

        binding.btnUpdatePrice.setOnClickListener {
            openCalculator()
        }

        binding.txtPrice.setOnClickListener {
            openCalculator()
        }

        binding.btnEditUnit.setOnClickListener {
            openSelectUnitDish()
        }

        binding.txtUnitName.setOnClickListener {
            openSelectUnitDish()
        }

        binding.btnDelete.setOnClickListener {
            deleteDish()
        }

        unitDishLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val unit = result.data?.getSerializableExtra("unit_data") as? Unit
                inventory.UnitID = unit!!.UnitID
                inventory.UnitName = unit.UnitName
                binding.txtUnitName.text = inventory.UnitName
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dish_form, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.btnSubmitDishFormToolbar -> {
                handleSubmitForm()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun openCalculator() {
        CalculatorDialogFragment.newInstance(
            inventory.Price.toString(),
            "Giá bán",
            "Số tiền",
            0.0,
            999999999.0)
        { result ->
            inventory.Price = result.toDouble()
            binding.txtPrice.text = FormatDisplay.formatNumber(result.toString())
        }.show(supportFragmentManager, null)
    }

    override fun openSelectUnitDish() {
        val intent = Intent(this, UnitActivity::class.java)
        intent.putExtra("unit_id_data", inventory.UnitID)
        intent.putExtra("unit_name_data", inventory.UnitName)
        unitDishLauncher.launch(intent)
    }

    @SuppressLint("InflateParams")
    private fun showColorPickerPopup() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_picker, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerColorPicker)

        recyclerView.visibility = View.VISIBLE
        dialogView.findViewById<RecyclerView>(R.id.recyclerImagePicker).visibility = View.GONE

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val colorList = (1..32).map { "color_$it" }

        val hexColorList = colorList.mapNotNull { colorName ->
            val colorId = resources.getIdentifier(colorName, "color", packageName)
            if (colorId != 0) {
                val colorInt = ContextCompat.getColor(this, colorId)
                String.format("#%06X", colorInt)
            } else null
        }

        val colorAdapter  = ListColorAdapter(this, hexColorList, inventory.Color).apply {
            onColorSelected = { color ->
                inventory.Color = color
                dialog.dismiss()
                setColorView(color)
            }
        }

        dialogView.findViewById<TextView>(R.id.btnCloseColorPicker)?.setOnClickListener {
            Toast.makeText(this, "close", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialogView.setBackgroundColor("#ffffffff".toColorInt())
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = colorAdapter
        dialog.show()
    }

    @SuppressLint("InflateParams")
    private fun showImagePickerPopup() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_picker, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerImagePicker)
        recyclerView.visibility = View.VISIBLE
        dialogView.findViewById<RecyclerView>(R.id.recyclerColorPicker).visibility = View.GONE


        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()


        val imgList = this.assets.list("icon_default")?.toList() ?: emptyList()


        val imageAdapter = ListImageAdapter(this, imgList).apply {
            onImageSelected = { image ->
                inventory.IconFileName = image
                dialog.dismiss()
                setImageView()
            }
        }

        dialogView.findViewById<TextView>(R.id.btnCloseColorPicker)?.setOnClickListener {
            dialog.dismiss()
        }
        dialogView.setBackgroundColor("#ffe6e6e6".toColorInt())

        recyclerView.layoutManager = GridLayoutManager(this, 5)
        recyclerView.adapter = imageAdapter
        dialog.show()
    }

    private fun getInventory() {
        val inventoryIdIntent = intent.getSerializableExtra("inventory_data") as? UUID
        if (inventoryIdIntent != null) {
            "Sửa món".also { binding.txtToolbarTitle.text = it }
            isAddNew = false
            val inventoryData = presenter.getInventory(inventoryIdIntent)
            if (inventoryData != null) {
                inventory = inventoryData
            }
            else {
                Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        else {
            "Thêm món".also { binding.txtToolbarTitle.text = it }
            binding.btnDelete.visibility = View.GONE
            binding.groupIsActive.visibility = View.GONE
        }
        setImageView()
        binding.txtUnitName.text = inventory.UnitName
        binding.txtPrice.text = FormatDisplay.formatNumber(inventory.Price.toString())
        binding.edtDishName.setText(inventory.InventoryName)
        binding.chkIsActiveDish.isChecked = !inventory.Inactive
        setColorView(inventory.Color)
    }

    private fun setImageView() {
        val imageView = binding.imgDish
        val drawable = ImageHelper.getDrawableImageFromAssets(this,inventory.IconFileName)
        imageView.setImageDrawable(drawable)
    }

    private fun setColorView(color: String) {
        val colorInt = color.toColorInt()
        binding.btnSelectColor.backgroundTintList = ColorStateList.valueOf(colorInt)
        binding.btnSelectImage.backgroundTintList = ColorStateList.valueOf(colorInt)
    }

    override fun handleSubmitForm() {
        inventory.InventoryName = binding.edtDishName.text.toString()
        if (!isAddNew) {
            inventory.Inactive = !binding.chkIsActiveDish.isChecked
        }
        val response = presenter.handleSubmitForm(inventory,isAddNew)
        if (response.isSuccess) {
            setResult(RESULT_OK)
            finish()
            return
        } else {
            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun deleteDish() {
        val response = presenter.handleDeleteInventory(inventory)
        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
        if (response.isSuccess) {
            setResult(RESULT_OK)
            finish()
            return
        }
    }
}