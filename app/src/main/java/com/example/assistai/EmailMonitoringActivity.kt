package com.example.assistai

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView

class EmailMonitoringActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_monitoring)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.btnFilter).setOnClickListener {
            Toast.makeText(this, "Filter clicked", Toast.LENGTH_SHORT).show()
        }

        val switchScanning = findViewById<SwitchCompat>(R.id.switchScanning)
        switchScanning.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "ON" else "OFF"
            Toast.makeText(this, "Gmail Scanning $status", Toast.LENGTH_SHORT).show()
        }

        val cardEmail1 = findViewById<MaterialCardView>(R.id.cardEmail1)
        findViewById<Button>(R.id.btnConfirm1).setOnClickListener {
            Toast.makeText(this, "Confirmed Sarah Miller's request", Toast.LENGTH_SHORT).show()
            cardEmail1.visibility = View.GONE
        }
        findViewById<Button>(R.id.btnDecline1).setOnClickListener {
            Toast.makeText(this, "Declined Sarah Miller's request", Toast.LENGTH_SHORT).show()
            cardEmail1.visibility = View.GONE
        }

        val cardEmail2 = findViewById<MaterialCardView>(R.id.cardEmail2)
        findViewById<Button>(R.id.btnConfirm2).setOnClickListener {
            Toast.makeText(this, "Confirmed David Green's request", Toast.LENGTH_SHORT).show()
            cardEmail2.visibility = View.GONE
        }
        findViewById<Button>(R.id.btnDecline2).setOnClickListener {
            Toast.makeText(this, "Declined David Green's request", Toast.LENGTH_SHORT).show()
            cardEmail2.visibility = View.GONE
        }

        val savedEmail = getSharedPreferences("AssistAiPrefs", MODE_PRIVATE).getString("linked_gmail", "admin@teachsync.ai")
        findViewById<TextView>(R.id.tvConnectedEmail).text = savedEmail

        findViewById<Button>(R.id.btnLinkGmail).setOnClickListener {
            connectGoogleAccount()
        }
    }

    private fun connectGoogleAccount() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_google_signin, null)
        bottomSheetDialog.setContentView(view)

        val tvAccount1 = view.findViewById<TextView>(R.id.tvAccount1)
        val tvAccount2 = view.findViewById<TextView>(R.id.tvAccount2)
        val tvConnectedEmail = findViewById<TextView>(R.id.tvConnectedEmail)

        tvAccount1.setOnClickListener {
            val email = tvAccount1.text.toString()
            tvConnectedEmail.text = email
            getSharedPreferences("AssistAiPrefs", MODE_PRIVATE).edit().putString("linked_gmail", email).apply()
            Toast.makeText(this, "Connected to $email", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }

        tvAccount2.setOnClickListener {
            val email = tvAccount2.text.toString()
            tvConnectedEmail.text = email
            getSharedPreferences("AssistAiPrefs", MODE_PRIVATE).edit().putString("linked_gmail", email).apply()
            Toast.makeText(this, "Connected to $email", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }
}
