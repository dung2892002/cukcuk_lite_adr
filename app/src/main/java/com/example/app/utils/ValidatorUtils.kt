package com.example.app.utils

object ValidatorUtils {
    fun isEmailOrPhone(input: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        val phoneRegex = Regex("^\\d{9,15}$") // từ 9 đến 15 chữ số

        return emailRegex.matches(input) || phoneRegex.matches(input)
    }

    fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")
        return passwordRegex.matches(password)
    }
}