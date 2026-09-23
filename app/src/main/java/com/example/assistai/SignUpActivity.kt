package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val cbTerms = findViewById<android.widget.CheckBox>(R.id.cbTerms)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val tvFooter = findViewById<TextView>(R.id.tvFooter)

        btnSignUp.setOnClickListener {
            val name = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            val isTermsChecked = cbTerms.isChecked

            when {
                name.isEmpty() -> {
                    etFullName.error = "Please enter your full name"
                    etFullName.requestFocus()
                }
                !ValidationUtils.isValidEmail(email) -> {
                    etEmail.error = "Please enter a valid email address"
                    etEmail.requestFocus()
                }
                ValidationUtils.validatePasswordStrength(password) != null -> {
                    etPassword.error = ValidationUtils.validatePasswordStrength(password)
                    etPassword.requestFocus()
                }
                confirmPassword != password -> {
                    etConfirmPassword.error = "Passwords do not match"
                    etConfirmPassword.requestFocus()
                }
                !isTermsChecked -> {
                    Toast.makeText(this, "Please agree to the Privacy Policy and Terms of Service", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Creating account...", Toast.LENGTH_SHORT).show()

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val uid = auth.currentUser?.uid ?: ""
                                val role = if (email.lowercase().contains("admin")) "admin" else "user"
                                val status = if (role == "admin") "approved" else "pending"

                                val userMap = hashMapOf(
                                    "uid" to uid,
                                    "name" to name,
                                    "email" to email,
                                    "role" to role,
                                    "status" to status
                                )

                                FirebaseFirestore.getInstance().collection("users").document(uid)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        if (role == "admin") {
                                            Toast.makeText(this, "Admin Account Registered Successfully!", Toast.LENGTH_LONG).show()
                                        } else {
                                            Toast.makeText(this, "Registration successful! Pending admin approval.", Toast.LENGTH_LONG).show()
                                        }
                                        auth.signOut()
                                        val intent = Intent(this, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Profile creation failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }

        tvFooter.setOnClickListener {
            finish()
        }
    }
}
