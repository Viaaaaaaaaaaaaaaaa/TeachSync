package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val tvFooter = findViewById<TextView>(R.id.tvFooter)

        btnBack.setOnClickListener {
            finish()
        }

        tvFooter.text = Html.fromHtml("Already have an account? <font color='#3B82F6'><b>Sign in</b></font>")
        tvFooter.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSignUp.setOnClickListener {
            val name = etFullName.text.toString().trim().ifEmpty { "Demo User" }
            val email = etEmail.text.toString().trim().ifEmpty { "demo.user@teachsync.ai" }
            val password = etPassword.text.toString().trim().ifEmpty { "Password123!" }

            Toast.makeText(this, "Welcome to TeachSync!", Toast.LENGTH_SHORT).show()

            // TEMPORARY BYPASS: Attempt registration silently, but always navigate directly to DashboardActivity
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        val uid = auth.currentUser?.uid ?: "bypass_uid"
                        FirebaseFirestore.getInstance().collection("users").document(uid).set(
                            hashMapOf(
                                "uid" to uid,
                                "name" to name,
                                "email" to email,
                                "role" to "user",
                                "status" to "approved"
                            )
                        )
                    }
            } catch (e: Exception) {
                // Ignore errors during temporary bypass mode
            }

            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
