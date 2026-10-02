package com.example.assistai

object ValidationUtils {

    // TEMPORARY BYPASS: Always return true for testing access
    fun isValidEmail(email: String): Boolean {
        return true
    }

    // TEMPORARY BYPASS: Always return null (no error) for testing access
    fun validatePasswordStrength(password: String): String? {
        return null
    }
}
