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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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
            val name = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            when {
                name.isEmpty() -> {
                    etFullName.error = "Please enter your username"
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
                else -> {
                    Toast.makeText(this, "Processing registration...", Toast.LENGTH_SHORT).show()

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val uid = auth.currentUser?.uid ?: ""
                                saveProfileAndNavigate(uid, name, email)
                            } else {
                                val exception = task.exception
                                if (exception is FirebaseAuthUserCollisionException ||
                                    exception?.message?.contains("already in use", ignoreCase = true) == true
                                ) {
                                    // Email already in use: automatically sign in with provided credentials
                                    Toast.makeText(this, "Email already exists. Logging in...", Toast.LENGTH_SHORT).show()
                                    auth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(this) { signInTask ->
                                            if (signInTask.isSuccessful) {
                                                val uid = auth.currentUser?.uid ?: ""
                                                saveProfileAndNavigate(uid, name, email)
                                            } else {
                                                Toast.makeText(
                                                    this,
                                                    "Account already exists. Please sign in on the login screen.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                val intent = Intent(this, MainActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            }
                                        }
                                } else {
                                    Toast.makeText(this, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                }
            }
        }
    }

    private fun saveProfileAndNavigate(uid: String, name: String, email: String) {
        val userMap = hashMapOf(
            "uid" to uid,
            "name" to name.ifEmpty { "User" },
            "email" to email,
            "role" to "user",
            "status" to "approved"
        )

        FirebaseFirestore.getInstance().collection("users").document(uid)
            .set(userMap)
            .addOnCompleteListener {
                Toast.makeText(this, "Welcome to TeachSync!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
    }
}
