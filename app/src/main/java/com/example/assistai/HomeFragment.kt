package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Feature Grid Cards
        val cardEmailMonitoring = view.findViewById<MaterialCardView>(R.id.cardEmailMonitoring)
        val cardSchedule = view.findViewById<MaterialCardView>(R.id.cardSchedule)
        val cardChatbot = view.findViewById<MaterialCardView>(R.id.cardChatbot)
        val cardRealtimeNotif = view.findViewById<MaterialCardView>(R.id.cardRealtimeNotif)

        // Recent Activity Items
        val itemActivity1 = view.findViewById<LinearLayout>(R.id.itemActivity1)
        val itemActivity2 = view.findViewById<LinearLayout>(R.id.itemActivity2)
        val itemActivity3 = view.findViewById<LinearLayout>(R.id.itemActivity3)

        // Set Feature Cards Click Listeners with Touch Animations
        cardEmailMonitoring.setOnClickListener {
            animateButtonClick(it) {
                val intent = Intent(requireContext(), EmailMonitoringActivity::class.java)
                startActivity(intent)
            }
        }

        cardSchedule.setOnClickListener {
            animateButtonClick(it) {
                (activity as? DashboardActivity)?.selectTab(R.id.nav_schedule)
            }
        }

        cardChatbot.setOnClickListener {
            animateButtonClick(it) {
                (activity as? DashboardActivity)?.selectTab(R.id.nav_chatbot)
            }
        }

        cardRealtimeNotif.setOnClickListener {
            animateButtonClick(it) {
                (activity as? DashboardActivity)?.selectTab(R.id.nav_alerts)
            }
        }

        // Set Activity Items Click Listeners with Touch Animations
        itemActivity1.setOnClickListener {
            animateButtonClick(it) {
                (activity as? DashboardActivity)?.selectTab(R.id.nav_chatbot)
            }
        }

        itemActivity2.setOnClickListener {
            animateButtonClick(it) {
                (activity as? DashboardActivity)?.selectTab(R.id.nav_schedule)
            }
        }

        itemActivity3.setOnClickListener {
            animateButtonClick(it) {
                (activity as? DashboardActivity)?.selectTab(R.id.nav_schedule)
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
