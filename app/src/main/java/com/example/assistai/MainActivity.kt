package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
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

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val tvForgot = findViewById<TextView>(R.id.tvForgot)
        val tvFooter = findViewById<TextView>(R.id.tvFooter)
        
        // Add a progress bar programmatically or dynamically if needed, or just use Toast for status feedback.
        // Let's check if there's a progress bar in layout or use Toast for feedback.

        tvFooter.text = Html.fromHtml("Don't have an account? <font color='#2E5FA3'><b>Sign Up</b></font>")
        tvFooter.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Check if user is already logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            handleUserRedirection(currentUser.uid)
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
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val role = document.getString("role") ?: "user"
                    val status = document.getString("status") ?: "pending"

                    if (role == "admin") {
                        val intent = Intent(this, AdminDashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        if (status == "approved") {
                            val intent = Intent(this, DashboardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            auth.signOut()
                            Toast.makeText(this, "Your account is pending admin approval.", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "User profile not found.", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching profile: ${e.message}", Toast.LENGTH_SHORT).show()
                auth.signOut()
            }
    }
}
