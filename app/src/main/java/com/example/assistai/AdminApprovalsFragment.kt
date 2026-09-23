package com.example.assistai

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class AdminApprovalsFragment : Fragment() {

    private lateinit var containerPendingUsers: LinearLayout
    private lateinit var tvEmptyState: TextView
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_approvals, container, false)

        db = FirebaseFirestore.getInstance()
        containerPendingUsers = view.findViewById(R.id.containerPendingUsers)
        tvEmptyState = view.findViewById(R.id.tvEmptyState)

        loadPendingUsers()

        return view
    }

    private fun loadPendingUsers() {
        db.collection("users")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    context?.let {
                        Toast.makeText(it, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                    return@addSnapshotListener
                }

                containerPendingUsers.removeAllViews()
                var pendingCount = 0

                if (snapshots != null) {
                    for (document in snapshots) {
                        val role = document.getString("role") ?: "user"
                        val status = document.getString("status") ?: "pending"

                        // Show anyone who is a user and isn't approved yet
                        if (role == "user" && status != "approved") {
                            pendingCount++
                            val uid = if (!document.getString("uid").isNullOrEmpty()) document.getString("uid")!! else document.id
                            val name = document.getString("name") ?: "Pending User"
                            val email = document.getString("email") ?: "No Email"

                            addUserRow(uid, name, email)
                        }
                    }
                }
                
                tvEmptyState.visibility = if (pendingCount == 0) View.VISIBLE else View.GONE
            }
    }

    private fun addUserRow(uid: String, name: String, email: String) {
        val rowLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 24)
            }
            setBackgroundResource(R.drawable.bg_feature_card)
            setPadding(32, 32, 32, 32)
            gravity = Gravity.CENTER_VERTICAL
        }

        val infoLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        }

        val tvName = TextView(requireContext()).apply {
            text = name
            setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 16f)
            setTextColor(Color.BLACK)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val tvEmail = TextView(requireContext()).apply {
            text = email
            setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14f)
            setTextColor(Color.GRAY)
        }

        infoLayout.addView(tvName)
        infoLayout.addView(tvEmail)

        val btnApprove = Button(requireContext()).apply {
            text = "Approve"
            isAllCaps = false
            setBackgroundResource(R.drawable.bg_button)
            setTextColor(Color.WHITE)
            setPadding(24, 0, 24, 0)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                approveUser(uid)
            }
        }

        rowLayout.addView(infoLayout)
        rowLayout.addView(btnApprove)

        containerPendingUsers.addView(rowLayout)
    }

    private fun approveUser(uid: String) {
        db.collection("users").document(uid)
            .update("status", "approved")
            .addOnSuccessListener {
                context?.let {
                    Toast.makeText(it, "User approved successfully!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                context?.let {
                    Toast.makeText(it, "Approval failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
