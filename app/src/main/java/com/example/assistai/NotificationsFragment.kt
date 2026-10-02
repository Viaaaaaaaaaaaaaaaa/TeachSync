package com.example.assistai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

class NotificationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        val btnMarkAllRead = view.findViewById<View>(R.id.btnMarkAllRead)
        val cardNotif1 = view.findViewById<View>(R.id.cardNotif1)
        val cardNotif2 = view.findViewById<View>(R.id.cardNotif2)
        val cardNotif3 = view.findViewById<View>(R.id.cardNotif3)

        val dotUnread1 = view.findViewById<View>(R.id.dotUnread1)
        val dotUnread2 = view.findViewById<View>(R.id.dotUnread2)
        val dotUnread3 = view.findViewById<View>(R.id.dotUnread3)

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
