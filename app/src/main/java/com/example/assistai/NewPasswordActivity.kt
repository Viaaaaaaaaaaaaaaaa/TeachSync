package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NewPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val etNewPassword = findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnResetPassword = findViewById<Button>(R.id.btnResetPassword)

        btnBack.setOnClickListener {
            finish()
        }

        btnResetPassword.setOnClickListener {
            val pass = etNewPassword.text.toString()
            val confirmPass = etConfirmPassword.text.toString()

            val strengthError = ValidationUtils.validatePasswordStrength(pass)
            if (strengthError != null) {
                etNewPassword.error = strengthError
                etNewPassword.requestFocus()
            } else if (confirmPass.isEmpty()) {
                etConfirmPassword.error = "Please confirm password"
                etConfirmPassword.requestFocus()
            } else if (pass != confirmPass) {
                etConfirmPassword.error = "Passwords do not match"
                etConfirmPassword.requestFocus()
            } else {
                val currentTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
                getSharedPreferences("AssistAiPrefs", MODE_PRIVATE).edit().putString("last_password_reset", currentTime).apply()
                val intent = Intent(this, PasswordResetSuccessActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
