package com.example.assistai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class NotificationsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        view.findViewById<TextView>(R.id.tvMarkAllRead).setOnClickListener {
            Toast.makeText(requireContext(), "All notifications marked as read", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
