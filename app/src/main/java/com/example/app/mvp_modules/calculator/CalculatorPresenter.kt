package com.example.app.mvp_modules.calculator

import com.example.app.utils.FormatDisplay

class CalculatorPresenter(private val view: CalculatorContract.View) : CalculatorContract.Presenter {
    private var input = ""
    private var maxLengthValue = 0

    override fun onButtonClicked(value: String) {
        if (view.isFirstInput()) {
            if (value.all { it.isDigit() } || value == "000") {
                input = if (value == "000") "0" else value
                view.setFirstInputDone()
                view.updateDisplay(input)
                return
            } else {
                view.setFirstInputDone()
            }
        }

        when (value) {
            "C" -> input = "0"
            "Xóa" -> {
                val tmp = input.dropLast(1)
                input = tmp.ifEmpty { "0" }
            }
            "Giảm" -> {
                input = evaluate(input)
                input = if (input.toDouble() > 1) (input.toDouble() - 1).toString() else "0"
                view.onCalculateState(false)
            }
            "Tăng" -> {
                input = evaluate(input)
                input = (input.toDouble() + 1).toString()
                view.onCalculateState(false)
            }
            "+" -> {
                view.onCalculateState(true)
                if (input.last() == '+' || input.last() == '.' || input.last() == '-') {
                    input = input.dropLast(1)
                }
                input += "+"
            }
            "-" -> {
                view.onCalculateState(true)
                if (input.last() == '+' || input.last() == '.' || input.last() == '-') {
                    input = input.dropLast(1)
                }
                input += "-"
            }
            "=" -> {
                input = evaluate(input)
                view.onCalculateState(false)
            }
            "±" -> {
                input = evaluate(input)
                if (input.first() == '-') input.drop(1)
                else input = "-$input"
                view.onCalculateState(false)
            }
            "," -> {
                if (isValidDecimalInput(input)) input += "."
            }
            "Xong" -> {
                view.showResult(input.toDouble())
            }
            else -> {
                if (input == "0") {
                    if (value == "0" || value == "000") return
                    input = value
                } else {
                    if (!hasTwoDecimalPlaces(input) && checkMaxLengthInput())
                        input += value
                }

            }
        }
        view.updateDisplay(formatOutput(input))
    }

    override fun setMaxValue(value: Double) {
        maxLengthValue = FormatDisplay.formatExpression(value.toString()).length
    }

    override fun setInput(value: String) {
        input = value
    }

    override fun close() {
        view.close()
    }

    private fun checkMaxLengthInput() : Boolean {
        if (input.contains('+') || input.contains('-')) {
            val last = Regex("(?=[+-])").split(input).last()
            return last.length < maxLengthValue - 1
        }
        return input.length < maxLengthValue - 1

    }

    private fun formatOutput(input: String) : String {
        return FormatDisplay.formatExpression(input)
    }

    private fun isValidDecimalInput(input: String) : Boolean {
        for (char in input.reversed()) {
            if (char == '.') return false
            if (char == '+' || char == '-') return true
        }
        return true
    }

    fun hasTwoDecimalPlaces(input: String): Boolean {
        val parts = Regex("(?=[+-])").split(input).last().split(Regex("\\."))
        return parts.size == 2 && parts[1].length == 2
    }

    private fun evaluate(expr: String): String {
        val result = object {
            var pos = -1
            var ch: Int = 0

            fun nextChar() {
                ch = if (++pos < expr.length) expr[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expr.length) throw RuntimeException("Unexpected: ${expr[pos]}")
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    when {
                        eat('+'.code) -> x += parseTerm()
                        eat('-'.code) -> x -= parseTerm()
                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    when {
                        eat('*'.code) -> x *= parseFactor()
                        eat('/'.code) -> x /= parseFactor()
                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return -parseFactor()

                var x: Double
                val startPos = pos
                if (eat('('.code)) {
                    x = parseExpression()
                    eat(')'.code)
                } else if (ch in '0'.code..'9'.code || ch == '.'.code) {
                    while (ch in '0'.code..'9'.code || ch == '.'.code) nextChar()
                    x = expr.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: ${ch.toChar()}")
                }
                return x
            }
        }.parse()

        var roundedResult = "%.2f".format(result)
        while (roundedResult.last() == '0' && roundedResult.contains('.')) {
            roundedResult = roundedResult.dropLast(1)
        }

        if (roundedResult.last() == '.') {
            roundedResult = roundedResult.dropLast(1)
        }
        return roundedResult
    }

}
