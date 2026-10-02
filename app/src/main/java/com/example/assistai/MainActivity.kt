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
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (!ValidationUtils.isValidEmail(email)) {
                etEmail.error = "Please enter a valid email address"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            val passwordError = ValidationUtils.validatePasswordStrength(password)
            if (passwordError != null) {
                etPassword.error = passwordError
                etPassword.requestFocus()
                return@setOnClickListener
            }

            Toast.makeText(this, "Signing in...", Toast.LENGTH_SHORT).show()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            handleUserRedirection(user.uid)
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        tvForgot.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleUserRedirection(uid: String) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(uid)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val status = document.getString("status") ?: "pending"
                    if (status != "approved") {
                        userRef.update("status", "approved")
                    }
                } else {
                    val userMap = hashMapOf(
                        "uid" to uid,
                        "email" to (auth.currentUser?.email ?: ""),
                        "role" to "user",
                        "status" to "approved"
                    )
                    userRef.set(userMap)
                }
                navigateToUserDashboard()
            }
            .addOnFailureListener {
                navigateToUserDashboard()
            }
    }

    private fun navigateToUserDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
