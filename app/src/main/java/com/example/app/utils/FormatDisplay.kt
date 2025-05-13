package com.example.app.utils
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Locale

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

    fun formatDateTimeCompat(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy (HH:mm)", Locale.getDefault())

        val date = inputFormat.parse(input)
        return outputFormat.format(date!!)
    }

    fun formatTo12HourWithCustomAMPM(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy (hh:mm a)", Locale.getDefault())

        val date = inputFormat.parse(input) ?: return ""

        var formatted = outputFormat.format(date)

        formatted = formatted.replace("AM", "SA").replace("PM", "CH")

        return formatted
    }
}