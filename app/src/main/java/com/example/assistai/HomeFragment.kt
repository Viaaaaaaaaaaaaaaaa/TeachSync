package com.example.assistai

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView

class HomeFragment : Fragment() {

    private var pendingBookingsCount = 3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Top Pill Buttons
        val btnClassesToday = view.findViewById<MaterialCardView>(R.id.btnClassesToday)
        val btnPendingBookings = view.findViewById<MaterialCardView>(R.id.btnPendingBookings)
        val btnAiResponses = view.findViewById<MaterialCardView>(R.id.btnAiResponses)

        // Feature Grid Cards
        val cardEmailMonitoring = view.findViewById<MaterialCardView>(R.id.cardEmailMonitoring)
        val cardSchedule = view.findViewById<MaterialCardView>(R.id.cardSchedule)
        val cardChatbot = view.findViewById<MaterialCardView>(R.id.cardChatbot)
        val cardRealtimeNotif = view.findViewById<MaterialCardView>(R.id.cardRealtimeNotif)

        // Recent Activity Items
        val itemActivity1 = view.findViewById<LinearLayout>(R.id.itemActivity1)
        val itemActivity2 = view.findViewById<LinearLayout>(R.id.itemActivity2)
        val itemActivity3 = view.findViewById<LinearLayout>(R.id.itemActivity3)

        // Set Top Pill Click Listeners with Touch Animations
        btnClassesToday.setOnClickListener {
            animateButtonClick(it) {
                showClassesTodayDialog()
            }
        }

        btnPendingBookings.setOnClickListener {
            animateButtonClick(it) {
                showPendingBookingsDialog()
            }
        }

        btnAiResponses.setOnClickListener {
            animateButtonClick(it) {
                showAiResponsesDialog()
            }
        }

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
                showAiResponsesDialog()
            }
        }

        itemActivity2.setOnClickListener {
            animateButtonClick(it) {
                (activity as? DashboardActivity)?.selectTab(R.id.nav_schedule)
            }
        }

        itemActivity3.setOnClickListener {
            animateButtonClick(it) {
                showPendingBookingsDialog()
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

    private fun showClassesTodayDialog() {
        val bottomSheet = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_classes_today, null)
        bottomSheet.setContentView(dialogView)

        val btnOpenFullSchedule = dialogView.findViewById<Button>(R.id.btnOpenFullSchedule)
        btnOpenFullSchedule?.setOnClickListener {
            bottomSheet.dismiss()
            (activity as? DashboardActivity)?.selectTab(R.id.nav_schedule)
        }

        bottomSheet.show()
    }

    private fun showPendingBookingsDialog() {
        val bottomSheet = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_pending_bookings, null)
        bottomSheet.setContentView(dialogView)

        val btnApprove1 = dialogView.findViewById<Button>(R.id.btnApproveBooking1)
        val btnDecline1 = dialogView.findViewById<Button>(R.id.btnDeclineBooking1)
        val containerBooking1 = dialogView.findViewById<View>(R.id.containerBooking1)

        btnApprove1?.setOnClickListener {
            containerBooking1?.visibility = View.GONE
            pendingBookingsCount = maxOf(0, pendingBookingsCount - 1)
            Toast.makeText(requireContext(), "Booking Approved!", Toast.LENGTH_SHORT).show()
        }

        btnDecline1?.setOnClickListener {
            containerBooking1?.visibility = View.GONE
            pendingBookingsCount = maxOf(0, pendingBookingsCount - 1)
            Toast.makeText(requireContext(), "Booking Declined.", Toast.LENGTH_SHORT).show()
        }

        bottomSheet.show()
    }

    private fun showAiResponsesDialog() {
        val bottomSheet = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_ai_responses, null)
        bottomSheet.setContentView(dialogView)

        val btnOpenChatbot = dialogView.findViewById<Button>(R.id.btnOpenChatbot)
        btnOpenChatbot?.setOnClickListener {
            bottomSheet.dismiss()
            (activity as? DashboardActivity)?.selectTab(R.id.nav_chatbot)
        }

        bottomSheet.show()
    }
}
