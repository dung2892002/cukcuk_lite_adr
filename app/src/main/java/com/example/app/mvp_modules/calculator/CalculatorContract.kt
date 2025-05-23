package com.example.app.mvp_modules.calculator

interface CalculatorContract {
    interface View {
        fun updateDisplay(value: String)
        fun showResult(result: Double)
        fun showError(message: String)
        fun close()
        fun onCalculateState(state: Boolean)
        fun isFirstInput(): Boolean
        fun setFirstInputDone()

    }

    interface Presenter {
        fun onButtonClicked(value: String)
        fun setInput(value: String)
        fun setMaxValue(maxValue: Double)
        fun close()
    }
}