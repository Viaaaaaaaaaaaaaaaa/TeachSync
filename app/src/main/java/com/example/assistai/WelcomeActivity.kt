package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val layoutLogo = findViewById<View>(R.id.layoutLogo)
        val layoutTitles = findViewById<View>(R.id.layoutTitles)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val btnEmailAuth = findViewById<ImageView>(R.id.btnEmailAuth)
        val btnPhoneAuth = findViewById<ImageView>(R.id.btnPhoneAuth)
        val tvSignUpNow = findViewById<TextView>(R.id.tvSignUpNow)

        // Staggered fade-in and slide-up entrance animations
        layoutLogo.alpha = 0f
        layoutLogo.translationY = -50f
        layoutLogo.animate().alpha(1f).translationY(0f).duration = 600

        layoutTitles.alpha = 0f
        layoutTitles.translationY = 50f
        layoutTitles.animate().alpha(1f).translationY(0f).setStartDelay(200).duration = 600

        btnSignIn.alpha = 0f
        btnSignIn.scaleX = 0.8f
        btnSignIn.scaleY = 0.8f
        btnSignIn.animate().alpha(1f).scaleX(1f).scaleY(1f).setStartDelay(400).duration = 500

        btnSignIn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnEmailAuth.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnPhoneAuth.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        tvSignUpNow.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
