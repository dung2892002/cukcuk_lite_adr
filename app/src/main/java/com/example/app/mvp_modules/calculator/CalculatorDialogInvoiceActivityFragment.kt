package com.example.app.mvp_modules.calculator

import android.os.Bundle
import com.example.app.R


class CalculatorDialogInvoiceActivityFragment : BaseCalculatorDialogFragment(){
    override fun getLayoutId(): Int {
        return R.layout.fragment_calculator_dialog_invoice_activity
    }

    companion object {
        fun newInstance(
            initialValue: String,
            title: String,
            messageName: String,
            minValue: Double,
            maxValue: Double,
            onResult: (Double) -> Unit
        ): CalculatorDialogInvoiceActivityFragment {
            val fragment = CalculatorDialogInvoiceActivityFragment()
            fragment.arguments = Bundle().apply {
                putString("initial_value", initialValue)
                putString("title", title)
                putDouble("minValue", minValue)
                putDouble("maxValue", maxValue)
                putString("messageName", messageName)
            }
            fragment.onResultCallback = onResult
            return fragment
        }
    }
}
