package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VerifyOtpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnVerify = findViewById<Button>(R.id.btnVerify)

        val etOtp1 = findViewById<EditText>(R.id.etOtp1)
        val etOtp2 = findViewById<EditText>(R.id.etOtp2)
        val etOtp3 = findViewById<EditText>(R.id.etOtp3)
        val etOtp4 = findViewById<EditText>(R.id.etOtp4)
        val etOtp5 = findViewById<EditText>(R.id.etOtp5)
        val etOtp6 = findViewById<EditText>(R.id.etOtp6)

        setupOtpFocus(etOtp1, etOtp2)
        setupOtpFocus(etOtp2, etOtp3)
        setupOtpFocus(etOtp3, etOtp4)
        setupOtpFocus(etOtp4, etOtp5)
        setupOtpFocus(etOtp5, etOtp6)

        btnBack.setOnClickListener {
            finish()
        }

        btnVerify.setOnClickListener {
            val otp = etOtp1.text.toString() + etOtp2.text.toString() + etOtp3.text.toString() +
                    etOtp4.text.toString() + etOtp5.text.toString() + etOtp6.text.toString()

            if (otp.length < 6) {
                Toast.makeText(this, "Please enter all 6 digits", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, NewPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupOtpFocus(current: EditText, next: EditText) {
        current.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) {
                    next.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
