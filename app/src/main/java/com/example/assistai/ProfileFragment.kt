package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvName = view.findViewById<TextView>(R.id.tvProfileName)
        val tvLinkedGmailSub = view.findViewById<TextView>(R.id.tvLinkedGmailSub)

        val cardProfileHeader = view.findViewById<View>(R.id.cardProfileHeader)
        val cardPromoBanner = view.findViewById<View>(R.id.cardPromoBanner)

        val btnPersonalInfo = view.findViewById<View>(R.id.btnPersonalInfo)
        val btnLinkedGmail = view.findViewById<View>(R.id.btnLinkedGmail)
        val btnResetPassword = view.findViewById<View>(R.id.btnResetPassword)
        val btnNotifSettings = view.findViewById<View>(R.id.btnNotifSettings)
        val btnLogout = view.findViewById<View>(R.id.btnLogout)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userEmail = currentUser.email ?: "user@teachsync.ai"
            tvLinkedGmailSub.text = userEmail

            FirebaseFirestore.getInstance().collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { doc ->
                    if (doc != null && doc.exists()) {
                        val name = doc.getString("name")
                        if (!name.isNullOrEmpty()) {
                            tvName.text = name
                        }
                    }
                }
        }

        cardProfileHeader?.setOnClickListener {
            animateButtonClick(it) {
                Toast.makeText(requireContext(), "Showing profile details for ${tvName.text}", Toast.LENGTH_SHORT).show()
            }
        }

        cardPromoBanner?.setOnClickListener {
            animateButtonClick(it) {
                (activity as? DashboardActivity)?.selectTab(R.id.nav_schedule)
            }
        }

        btnPersonalInfo?.setOnClickListener {
            animateButtonClick(it) {
                Toast.makeText(requireContext(), "User Role: Teacher / Faculty | Email: ${currentUser?.email ?: "user@teachsync.ai"}", Toast.LENGTH_LONG).show()
            }
        }

        btnLinkedGmail?.setOnClickListener {
            animateButtonClick(it) {
                val intent = Intent(requireContext(), EmailMonitoringActivity::class.java)
                startActivity(intent)
            }
        }

        btnResetPassword?.setOnClickListener {
            animateButtonClick(it) {
                val intent = Intent(requireContext(), ForgotPasswordActivity::class.java)
                startActivity(intent)
            }
        }

        btnNotifSettings?.setOnClickListener {
            animateButtonClick(it) {
                (activity as? DashboardActivity)?.selectTab(R.id.nav_alerts)
            }
        }

        btnLogout?.setOnClickListener {
            animateButtonClick(it) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to sign out of TeachSync?")
                    .setPositiveButton("Log Out") { _, _ ->
                        auth.signOut()
                        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        activity?.finish()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }

        return view
    }

    private fun animateButtonClick(view: View, onAnimationEnd: () -> Unit) {
        view.animate()
            .scaleX(0.92f)
            .scaleY(0.92f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(120)
                    .withEndAction {
                        onAnimationEnd()
                    }
                    .start()
            }
            .start()
    }
}
