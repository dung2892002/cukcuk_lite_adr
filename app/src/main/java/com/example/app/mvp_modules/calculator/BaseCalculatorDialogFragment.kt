package com.example.app.mvp_modules.calculator

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.graphics.toColorInt
import androidx.fragment.app.DialogFragment
import com.example.app.R
import com.example.app.utils.FormatDisplay

abstract class BaseCalculatorDialogFragment : DialogFragment(), CalculatorContract.View {

    protected lateinit var presenter: CalculatorContract.Presenter
    protected lateinit var display: EditText
    protected lateinit var buttonSubmit: Button
    protected var buttonCalculate: Button? = null
    protected var buttonClose: ImageButton? = null

    protected var initialValue = ""
    protected var messageName = ""
    protected var minValue = 0.0
    protected var maxValue = 999999999.0
    protected var title = ""
    protected var firstInput = true
    protected var onResultCallback: ((Double) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialValue = arguments?.getString("initial_value") ?: ""
        messageName = arguments?.getString("messageName") ?: ""
        title = arguments?.getString("title") ?: ""
        minValue = arguments?.getDouble("minValue") ?: 0.0
        maxValue = arguments?.getDouble("maxValue") ?: 999999999.0

        while (initialValue.endsWith("0") && initialValue.contains('.')) {
            initialValue = initialValue.dropLast(1)
        }
        if (initialValue.endsWith(".")) {
            initialValue = initialValue.dropLast(1)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(getLayoutId(), null)
        setupViews(view)
        initPresenter()

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(true)
            .create()
    }

    private fun setupViews(view: View) {
        display = view.findViewById(R.id.tvDisplay)
        val grid = view.findViewById<GridLayout>(R.id.gridButtons)
        buttonSubmit = view.findViewById(R.id.btnSubmitCalculator)
        buttonCalculate = view.findViewById(R.id.btnHandleCalculator)
        buttonClose = view.findViewById(R.id.btnCloseCalculator)

        display.setText(FormatDisplay.formatExpression(initialValue))
        display.requestFocus()
        display.selectAll()
        display.showSoftInputOnFocus = false
        display.isCursorVisible = false
        display.highlightColor = "#BBCDE4".toColorInt()

        for (i in 0 until grid.childCount) {
            val viewChild = grid.getChildAt(i)
            val value = when (viewChild) {
                is Button -> viewChild.text.toString()
                is ImageButton -> viewChild.contentDescription?.toString()
                else -> null
            }
            if (value != null) {
                viewChild.setOnClickListener { presenter.onButtonClicked(value) }
            }
        }

        view.findViewById<TextView>(R.id.calculatorTitle).text = title

        buttonSubmit.setOnClickListener {
            presenter.onButtonClicked("Xong")
        }

        buttonCalculate?.setOnClickListener {
            presenter.onButtonClicked("=")
        }

        buttonClose?.setOnClickListener {
            close()
        }

    }

    private fun initPresenter() {
        presenter = CalculatorPresenter(this)
        presenter.setInput(initialValue)
        presenter.setMaxValue(maxValue)
    }

    override fun isFirstInput(): Boolean {
        return firstInput
    }

    override fun setFirstInputDone() {
        firstInput = false
    }

    override fun updateDisplay(value: String) {
        display.setText(value)
    }

    override fun showResult(result: Double) {
        if (result < minValue) {
            Toast.makeText(
                requireContext(),
                "$messageName phải lớn hơn hoặc bằng ${FormatDisplay.formatNumber(minValue.toString())}.\nVui lòng thử lại",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (result > maxValue) {
            Toast.makeText(
                requireContext(),
                "$messageName không được lớn hơn ${FormatDisplay.formatNumber(maxValue.toString())}.\nVui lòng thử lại",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        onResultCallback?.invoke(result)
        close()
    }

    override fun showError(message: String) {
        display.setText(message)
    }

    override fun close() {
        dismiss()
    }

    override fun onCalculateState(state: Boolean) {
        if (state) {
            buttonCalculate?.visibility = View.VISIBLE
            buttonSubmit.visibility = View.GONE
        } else {
            buttonCalculate?.visibility = View.GONE
            buttonSubmit.visibility = View.VISIBLE
        }
    }

    abstract fun getLayoutId(): Int
}
