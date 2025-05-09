package com.example.app.mvp_modules.menu.views

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.R
import com.example.app.databinding.ActivityDishFormBinding
import com.example.app.models.Dish
import com.example.app.mvp_modules.menu.contracts.DishFormContract
import com.example.app.mvp_modules.menu.presenters.DishFormPresenter
import java.util.UUID
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.mvp_modules.menu.adapters.ListColorAdapter
import com.example.app.mvp_modules.menu.adapters.ListImageAdapter
import com.example.app.utils.ImageHelper

class DishFormActivity : AppCompatActivity(), DishFormContract.View {
    private lateinit var binding: ActivityDishFormBinding
    private lateinit var presenter: DishFormContract.Presenter
    private var dish: Dish = Dish(UUID.randomUUID(),"",0.0,"Chai","#FFFA8072","ic_default.png")
    private var isAddNew: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDishFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = DishFormPresenter(this)

        getDish()
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
                String.format("#%08X", colorInt)
            } else null
        }

        val colorAdapter  = ListColorAdapter(this, hexColorList, dish.color).apply {
            onColorSelected = { color ->
                dish.color = color
                dialog.dismiss()
                setColor(color)
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
                dish.image = image
                dialog.dismiss()
                setImage()
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

    private fun getDish() {
        if (intent.getSerializableExtra("dish") as? Dish != null) {
            "Sửa món".also { binding.txtToolbarTitle.text = it }
            isAddNew = false
        }
        else {
            "Thêm món".also { binding.txtToolbarTitle.text = it }
        }
        setImage()
        binding.txtUnitName.text = dish.unit
        setColor(dish.color)
    }

    private fun setImage() {
        val imageView = binding.imgDish // thêm thư mục con
        val drawable = ImageHelper.getDrawableImageFromAssets(this,dish.image)
        imageView.setImageDrawable(drawable)
    }

    private fun setColor(color: String) {
        val colorInt = color.toColorInt()
        binding.btnSelectColor.backgroundTintList = ColorStateList.valueOf(colorInt)
        binding.btnSelectImage.backgroundTintList = ColorStateList.valueOf(colorInt)
    }

    override fun handleSubmitForm() {
        dish.name = binding.edtDishName.text.toString()
        presenter.submitForm(dish,isAddNew)
    }

    override fun handleSubmitFormResult(isSuccess: Boolean, message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        if (isSuccess) {
            finish()
            return
        }
    }
}