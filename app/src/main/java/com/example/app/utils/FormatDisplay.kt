package com.example.app.utils
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Locale

object FormatDisplay {
    fun formatNumber(number: String): String {
        var resultValue = number

        // Trường hợp kết thúc bằng dấu chấm, đánh dấu để giữ dấu ',' sau này
        val endsWithDot = resultValue.endsWith(".")

        // Xoá số 0 không cần thiết sau dấu thập phân
        if (resultValue.contains('.')) {
            while (resultValue.endsWith("0")) {
                resultValue = resultValue.dropLast(1)
            }
            // Nếu còn dấu '.' ở cuối, tạm xoá để parse số
            if (resultValue.endsWith(".")) {
                resultValue = resultValue.dropLast(1)
            }
        }

        // Nếu chuỗi còn lại rỗng hoặc không hợp lệ
        val doubleValue = resultValue.toDoubleOrNull() ?: return "0"

        // Định dạng với dấu phẩy
        val symbols = DecimalFormatSymbols().apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }

        val formatter = DecimalFormat("#,##0.##", symbols)
        var formatted = formatter.format(doubleValue)

        // Nếu ban đầu có dấu '.' ở cuối thì thêm dấu ',' ở cuối
        if (endsWithDot) {
            formatted += ","
        }

        return formatted
    }

    fun formatExpression(expression: String): String {
        if (expression.isBlank()) return ""

        val regex = Regex("(?=[+-])")
        val parts = regex.split(expression).filter { it.isNotEmpty() }

        val formattedParts = parts.map { part ->
            val operator = if (part.startsWith('+') || part.startsWith('-')) part[0].toString() else ""
            val number = if (operator.isNotEmpty()) part.substring(1) else part

            val formattedNumber = if (number.isEmpty()) {
                // Giữ nguyên chuỗi rỗng nếu không có số phía sau toán tử
                ""
            } else {
                formatNumber(number)
            }
            operator + formattedNumber
        }

        return formattedParts.joinToString("")
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