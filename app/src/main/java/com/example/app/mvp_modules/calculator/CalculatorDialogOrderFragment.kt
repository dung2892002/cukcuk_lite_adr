package com.example.app.mvp_modules.calculator

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.app.R

class CalculatorDialogOrderFragment : DialogFragment(), CalculatorContract.View {
    private lateinit var presenter: CalculatorContract.Presenter
    private lateinit var display: TextView
    private lateinit var buttonSubmit: Button

    private var initialValue = ""
    private var title = ""
    private var onResultCallback: ((Double) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialValue = arguments?.getString("initial_value") ?: ""
        title = arguments?.getString("title") ?: ""


        while (initialValue.last() == '0' && initialValue.contains('.')) {
            initialValue = initialValue.dropLast(1)
        }

        if (initialValue.last() == '.') {
            initialValue = initialValue.dropLast(1)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.fragment_calculator_dialog_invoice, null)
        display = view.findViewById(R.id.tvDisplay)
        val grid = view.findViewById<GridLayout>(R.id.gridButtons)
        buttonSubmit = view.findViewById(R.id.btnSubmitCalculator)

        presenter = CalculatorPresenter(this)

        presenter.setInput(initialValue)

        display.text = initialValue

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


        view.findViewById<ImageView>(R.id.btnCloseCalculator).setOnClickListener {
            presenter.close()
        }

        view.findViewById<TextView>(R.id.calculatorTitle).text = title

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(true)
            .create()
    }

    override fun updateDisplay(value: String) {
        display.text = value
    }


    override fun showResult(result: Double) {
        onResultCallback?.invoke(result)
    }

    override fun showError(message: String) {
        display.text = message
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
            title: String,
            onResult: (Double) -> Unit
        ): CalculatorDialogOrderFragment {
            val fragment = CalculatorDialogOrderFragment()
            val bundle = Bundle().apply {
                putString("initial_value", initialValue)
                putString("title", title)
            }
            fragment.arguments = bundle
            fragment.onResultCallback = onResult
            return fragment
        }
    }

}