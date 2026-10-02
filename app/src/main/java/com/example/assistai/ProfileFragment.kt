package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        val tvEmail = view.findViewById<TextView>(R.id.tvProfileEmail)
        val tvLinkedGmailSub = view.findViewById<TextView>(R.id.tvLinkedGmailSub)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val btnLinkedGmail = view.findViewById<View>(R.id.btnLinkedGmail)
        val btnResetPassword = view.findViewById<View>(R.id.btnResetPassword)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            tvEmail.text = currentUser.email ?: "user@teachsync.ai"
            tvLinkedGmailSub.text = currentUser.email ?: "user@teachsync.ai"

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

        btnLinkedGmail.setOnClickListener {
            val intent = Intent(requireContext(), EmailMonitoringActivity::class.java)
            startActivity(intent)
        }

        btnResetPassword.setOnClickListener {
            val intent = Intent(requireContext(), ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
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

        return view
    }
}
