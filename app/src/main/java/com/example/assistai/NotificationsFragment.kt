package com.example.assistai

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView

class NotificationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        val btnMarkAllRead = view.findViewById<View>(R.id.btnMarkAllRead)
        val containerNotifications = view.findViewById<LinearLayout>(R.id.containerNotifications)
        val cardNotif1 = view.findViewById<View>(R.id.cardNotif1)
        val cardNotif2 = view.findViewById<View>(R.id.cardNotif2)
        val cardNotif3 = view.findViewById<View>(R.id.cardNotif3)

        val dotUnread1 = view.findViewById<View>(R.id.dotUnread1)
        val dotUnread2 = view.findViewById<View>(R.id.dotUnread2)
        val dotUnread3 = view.findViewById<View>(R.id.dotUnread3)

        // Check real-time AI query from chatbot
        val sharedPrefs = requireContext().getSharedPreferences("TeachSyncPrefs", Context.MODE_PRIVATE)
        val lastQuery = sharedPrefs.getString("last_ai_query", null)
        val lastTime = sharedPrefs.getLong("last_ai_time", 0L)

        if (lastQuery != null && (System.currentTimeMillis() - lastTime) < 86400000L) {
            // Dynamically prepend a real-time notification card for the AI query / classes today / pending bookings response
            val dynamicCard = MaterialCardView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 24
                }
                setCardBackgroundColor(android.graphics.Color.WHITE)
                radius = 40f
                elevation = 8f
                strokeWidth = 2
                strokeColor = android.graphics.Color.parseColor("#0B2317")
                isClickable = true
                isFocusable = true
            }

            val innerLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_VERTICAL
                setPadding(36, 36, 36, 36)
            }

            val textLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                setPadding(24, 0, 0, 0)
            }

            val tvTitle = TextView(requireContext()).apply {
                text = "Real-Time AI Response: $lastQuery"
                setTextColor(android.graphics.Color.parseColor("#111827"))
                textSize = 14f
                setTypeface(null, android.graphics.Typeface.BOLD)
            }

            val tvDesc = TextView(requireContext()).apply {
                text = "Successfully retrieved live query & schedule info from TeachSync AI assistant."
                setTextColor(android.graphics.Color.parseColor("#4B5563"))
                textSize = 12f
            }

            textLayout.addView(tvTitle)
            textLayout.addView(tvDesc)
            innerLayout.addView(textLayout)
            dynamicCard.addView(innerLayout)

            dynamicCard.setOnClickListener {
                animateButtonClick(it) {
                    Toast.makeText(requireContext(), "Viewing Real-Time AI Response: $lastQuery", Toast.LENGTH_SHORT).show()
                }
            }

            containerNotifications?.addView(dynamicCard, 0)
        }

        btnMarkAllRead?.setOnClickListener {
            animateButtonClick(it) {
                dotUnread1?.visibility = View.INVISIBLE
                dotUnread2?.visibility = View.INVISIBLE
                dotUnread3?.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), "All notifications marked as read", Toast.LENGTH_SHORT).show()
            }
        }

        cardNotif1?.setOnClickListener {
            animateButtonClick(it) {
                dotUnread1?.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), "Viewing: New Booking Email Scanned", Toast.LENGTH_SHORT).show()
            }
        }

        cardNotif2?.setOnClickListener {
            animateButtonClick(it) {
                dotUnread2?.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), "Viewing: Schedule Synced Successfully", Toast.LENGTH_SHORT).show()
            }
        }

        cardNotif3?.setOnClickListener {
            animateButtonClick(it) {
                dotUnread3?.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), "Viewing: AI Chatbot Auto-Response Sent", Toast.LENGTH_SHORT).show()
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
