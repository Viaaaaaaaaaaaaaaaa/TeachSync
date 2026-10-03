package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminHomeFragment : Fragment() {

    private lateinit var tvTodayCount: TextView
    private lateinit var tvUpcomingCount: TextView
    private lateinit var tvOnDutyCount: TextView
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        tvTodayCount = view.findViewById(R.id.tvTodayCount)
        tvUpcomingCount = view.findViewById(R.id.tvUpcomingCount)
        tvOnDutyCount = view.findViewById(R.id.tvOnDutyCount)

        val btnAdminSignOut = view.findViewById<MaterialCardView>(R.id.btnAdminSignOut)

        btnAdminSignOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        fetchLiveSystemMetrics()

        return view
    }

    private fun fetchLiveSystemMetrics() {
        db.collection("users").addSnapshotListener { snapshots, error ->
            if (error == null && snapshots != null) {
                var totalUsers = snapshots.size()
                tvTodayCount.text = totalUsers.toString()
                tvUpcomingCount.text = "2"
                tvOnDutyCount.text = "2/3"
            }
        }
    }
}
