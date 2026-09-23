package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnLogout = view.findViewById<ImageView>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }

        val cardEmailMonitoring = view.findViewById<MaterialCardView>(R.id.cardEmailMonitoring)
        cardEmailMonitoring.setOnClickListener {
            val intent = Intent(requireContext(), EmailMonitoringActivity::class.java)
            startActivity(intent)
        }

        val cardSchedule = view.findViewById<MaterialCardView>(R.id.cardSchedule)
        cardSchedule.setOnClickListener {
            (activity as? DashboardActivity)?.selectTab(R.id.nav_schedule)
        }

        val cardChatbot = view.findViewById<MaterialCardView>(R.id.cardChatbot)
        cardChatbot.setOnClickListener {
            (activity as? DashboardActivity)?.selectTab(R.id.nav_chatbot)
        }

        val cardAiAction = view.findViewById<MaterialCardView>(R.id.cardAiAction)
        cardAiAction.setOnClickListener {
            val progressDialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("AI Action Processing")
                .setMessage("Analyzing data...")
                .setCancelable(false)
                .create()
            progressDialog.show()

            val handler = android.os.Handler(android.os.Looper.getMainLooper())
            handler.postDelayed({
                progressDialog.setMessage("Optimizing schedules...")
            }, 1000)

            handler.postDelayed({
                progressDialog.setMessage("AI Actions completed successfully!")
            }, 2000)

            handler.postDelayed({
                progressDialog.dismiss()
                android.widget.Toast.makeText(requireContext(), "AI Actions completed successfully!", android.widget.Toast.LENGTH_SHORT).show()
            }, 3000)
        }

        val btnNotifications = view.findViewById<ImageView>(R.id.btnNotifications)
        btnNotifications.setOnClickListener {
            (activity as? DashboardActivity)?.selectTab(R.id.nav_alerts)
        }

        val btnSettings = view.findViewById<ImageView>(R.id.btnSettings)
        btnSettings.setOnClickListener {
            val intent = Intent(requireContext(), AccountSettingsActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
