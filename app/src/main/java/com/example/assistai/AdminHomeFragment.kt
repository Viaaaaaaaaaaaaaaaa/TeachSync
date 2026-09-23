package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminHomeFragment : Fragment() {

    private lateinit var tvTotalUsersCount: TextView
    private lateinit var tvPendingApprovalsCount: TextView
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        tvTotalUsersCount = view.findViewById(R.id.tvTotalUsersCount)
        tvPendingApprovalsCount = view.findViewById(R.id.tvPendingApprovalsCount)

        val btnAdminLogout = view.findViewById<ImageView>(R.id.btnAdminLogout)
        val adminCardApprovals = view.findViewById<MaterialCardView>(R.id.adminCardApprovals)
        val adminCardEmailControl = view.findViewById<MaterialCardView>(R.id.adminCardEmailControl)
        val adminCardScheduleControl = view.findViewById<MaterialCardView>(R.id.adminCardScheduleControl)
        val adminCardChatbotControl = view.findViewById<MaterialCardView>(R.id.adminCardChatbotControl)

        btnAdminLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        adminCardApprovals.setOnClickListener {
            (activity as? AdminDashboardActivity)?.selectTab(R.id.nav_admin_approvals)
        }

        val dummyClick = View.OnClickListener {
            Toast.makeText(requireContext(), "Master Admin Override active.", Toast.LENGTH_SHORT).show()
        }

        adminCardEmailControl.setOnClickListener(dummyClick)
        adminCardScheduleControl.setOnClickListener(dummyClick)
        adminCardChatbotControl.setOnClickListener(dummyClick)

        fetchLiveSystemMetrics()

        return view
    }

    private fun fetchLiveSystemMetrics() {
        // Query total registered users count
        db.collection("users").addSnapshotListener { snapshots, error ->
            if (error == null && snapshots != null) {
                tvTotalUsersCount.text = snapshots.size().toString()
                
                // Calculate dynamic non-approved user count
                var pendingCount = 0
                for (doc in snapshots) {
                    val role = doc.getString("role") ?: "user"
                    val status = doc.getString("status") ?: "pending"
                    if (role == "user" && status != "approved") {
                        pendingCount++
                    }
                }
                tvPendingApprovalsCount.text = pendingCount.toString()
            }
        }
    }
}
