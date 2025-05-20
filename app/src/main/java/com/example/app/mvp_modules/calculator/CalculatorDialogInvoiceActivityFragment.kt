package com.example.app.mvp_modules.calculator

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.fragment.app.DialogFragment
import com.example.app.R


class CalculatorDialogInvoiceActivityFragment : DialogFragment(), CalculatorContract.View {

    private lateinit var presenter: CalculatorContract.Presenter
    private lateinit var display: EditText
    private lateinit var buttonSubmit: Button

    private var initialValue = ""
    private var isFirstInput = true
    private var onResultCallback: ((Double) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialValue = arguments?.getString("initial_value")!!

        while (initialValue.last() == '0' && initialValue.contains('.')) {
            initialValue = initialValue.dropLast(1)
        }

        if (initialValue.last() == '.') {
            initialValue = initialValue.dropLast(1)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.fragment_calculator_dialog_invoice_receive_money, null)
        display = view.findViewById(R.id.tvDisplay)
        val grid = view.findViewById<GridLayout>(R.id.gridButtons)
        buttonSubmit = view.findViewById(R.id.btnSubmitCalculator)

        presenter = CalculatorPresenter(this)

        presenter.setInput(initialValue)

        display.setText(initialValue)
        display.requestFocus()
        display.selectAll()
        display.showSoftInputOnFocus = false
        display.isCursorVisible = false
        display.highlightColor = "#BBCDE4".toColorInt()

        for (i in 0 until grid.childCount) {
            val view = grid.getChildAt(i)

            val value = when (view) {
                is Button -> view.text.toString()
                is ImageButton -> view.contentDescription?.toString()
                else -> null
            }

            if (value != null) {
                view.setOnClickListener {
                    presenter.onButtonClicked(value)
                }
            }
        }

        buttonSubmit.setOnClickListener {
            presenter.onButtonClicked("Xong")
        }


        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(true)
            .create()
    }

    override fun isFirstInput(): Boolean {
        return isFirstInput
    }

    override fun setFirstInputDone() {
        isFirstInput = false
    }

    override fun updateDisplay(value: String) {
        display.setText(value)
    }


    override fun showResult(result: Double) {
        onResultCallback?.invoke(result)
    }

    override fun showError(message: String) {
        display.setText(message)
    }

    override fun close() {
        dismiss()
    }

    override fun onCalculateState(state: Boolean) {
        if (!state) {
            buttonSubmit.visibility = View.VISIBLE
        } else {
            buttonSubmit.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(
            initialValue: String,
            onResult: (Double) -> Unit
        ): CalculatorDialogInvoiceActivityFragment {
            val fragment = CalculatorDialogInvoiceActivityFragment()
            fragment.arguments = Bundle().apply {
                putString("initial_value", initialValue)
            }
            fragment.onResultCallback = onResult
            return fragment
        }
    }
}
