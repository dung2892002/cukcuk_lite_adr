package com.example.app.utils
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object FormatDisplay {
    fun formatNumber(number: String): String {
        var resultValue = number
        while (resultValue.last() == '0' && resultValue.contains('.')) {
            resultValue = resultValue.dropLast(1)
        }

        if (resultValue.last() == '.') {
            resultValue = resultValue.dropLast(1)
        }

        return try {
            val cleaned = resultValue.trimStart('0')
            val doubleValue = cleaned.toDoubleOrNull() ?: return "0"

            val symbols = DecimalFormatSymbols().apply {
                groupingSeparator = '.'
                decimalSeparator = ','
            }

            val formatter = DecimalFormat("#,##0.##", symbols)
            formatter.format(doubleValue)

        } catch (e: Exception) {
            "0"
        }
    }
}