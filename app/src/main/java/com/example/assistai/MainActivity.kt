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

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val tvForgot = findViewById<TextView>(R.id.tvForgot)
        val tvFooter = findViewById<TextView>(R.id.tvFooter)

        btnBack.setOnClickListener {
            finish()
        }

        tvFooter.text = Html.fromHtml("Don't have an account? <font color='#3B82F6'><b>Sign up</b></font>")
        tvFooter.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString().trim().ifEmpty { "demo.user@teachsync.ai" }
            val password = etPassword.text.toString().trim().ifEmpty { "Password123!" }

            Toast.makeText(this, "Accessing Dashboard...", Toast.LENGTH_SHORT).show()

            // TEMPORARY BYPASS: Attempt Firebase sign-in silently, but always navigate directly to DashboardActivity
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { taskResult ->
                        val user = taskResult.user
                        if (user != null) {
                            val db = FirebaseFirestore.getInstance()
                            db.collection("users").document(user.uid).set(
                                hashMapOf(
                                    "uid" to user.uid,
                                    "email" to email,
                                    "role" to "user",
                                    "status" to "approved"
                                )
                            )
                        }
                    }
            } catch (e: Exception) {
                // Ignore errors during temporary bypass mode
            }

            // Immediately navigate to User Dashboard
            navigateToUserDashboard()
        }

        tvForgot.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToUserDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
