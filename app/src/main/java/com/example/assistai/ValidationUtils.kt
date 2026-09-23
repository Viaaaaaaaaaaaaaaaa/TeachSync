package com.example.assistai

import android.util.Patterns

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePasswordStrength(password: String): String? {
        if (password.isEmpty()) return "Password cannot be empty"
        if (password.length < 8) return "Password must be at least 8 characters long"
        if (!password.any { it.isUpperCase() }) return "Password must contain at least one uppercase letter"
        if (!password.any { it.isLowerCase() }) return "Password must contain at least one lowercase letter"
        if (!password.any { it.isDigit() }) return "Password must contain at least one numeric digit"
        val specialChars = "!@#$%^&*(),.?\":{}|<>_+-=[]\\疲"
        if (!password.any { specialChars.contains(it) }) return "Password must contain at least one special character"
        return null
    }
}
