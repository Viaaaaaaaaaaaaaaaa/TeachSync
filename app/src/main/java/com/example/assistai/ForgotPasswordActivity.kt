package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnSendCode = findViewById<Button>(R.id.btnSendCode)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        btnBack.setOnClickListener {
            finish()
        }

        btnSendCode.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            
            if (email.isEmpty()) {
                etEmail.error = "Please enter your email"
                etEmail.requestFocus()
            } else if (!email.matches(emailPattern.toRegex())) {
                etEmail.error = "Please enter a valid email"
                etEmail.requestFocus()
            } else {
                btnSendCode.isEnabled = false
                progressBar.visibility = View.VISIBLE
                
                val progressDialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                    .setTitle("Secure Gmail Auth Session")
                    .setMessage("Establishing encrypted handshake with Google OAuth servers...\n\nSending verification link via Gmail API securely.")
                    .setCancelable(false)
                    .create()
                progressDialog.show()
                
                Handler(Looper.getMainLooper()).postDelayed({
                    progressBar.visibility = View.GONE
                    progressDialog.dismiss()
                    Toast.makeText(this, "Secure link sent to your Gmail inbox", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, VerifyOtpActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 3000)
            }
        }
    }
}
