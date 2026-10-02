package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PasswordResetSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset_success)

        val successIcon = findViewById<ImageView>(R.id.successIcon)
        val btnBackToLogin = findViewById<Button>(R.id.btnBackToLogin)

        // Entrance animation with OvershootInterpolator
        successIcon.alpha = 0f
        successIcon.scaleX = 0.4f
        successIcon.scaleY = 0.4f
        successIcon.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(700)
            .setInterpolator(android.view.animation.OvershootInterpolator(1.5f))
            .start()

        btnBackToLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
