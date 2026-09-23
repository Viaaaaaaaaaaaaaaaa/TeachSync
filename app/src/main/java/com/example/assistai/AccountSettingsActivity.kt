package com.example.assistai

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AccountSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val prefs = getSharedPreferences("AssistAiPrefs", MODE_PRIVATE)
        val linkedGmail = prefs.getString("linked_gmail", "admin.user@gmail.com")
        val lastPasswordReset = prefs.getString("last_password_reset", "Not reset recently")

        findViewById<TextView>(R.id.tvLinkedGmail).text = linkedGmail
        findViewById<TextView>(R.id.tvLastPasswordReset).text = lastPasswordReset
    }
}
