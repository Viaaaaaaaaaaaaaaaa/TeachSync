package com.example.assistai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog

class ScheduleFragment : Fragment() {

    private var selectedDayIndex = 1 // Monday default
    private lateinit var dayViews: List<LinearLayout>
    private lateinit var dayLabels: List<TextView>
    private lateinit var dayDates: List<TextView>

    private lateinit var tvDayHeader: TextView
    private lateinit var tvBookingCount: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)

        tvDayHeader = view.findViewById(R.id.tvDayHeader)
        tvBookingCount = view.findViewById(R.id.tvBookingCount)

        dayViews = listOf(
            view.findViewById(R.id.daySun),
            view.findViewById(R.id.dayMon),
            view.findViewById(R.id.dayTue),
            view.findViewById(R.id.dayWed),
            view.findViewById(R.id.dayThu),
            view.findViewById(R.id.dayFri),
            view.findViewById(R.id.daySat)
        )

        dayLabels = listOf(
            view.findViewById(R.id.tvSunLabel),
            view.findViewById(R.id.tvMonLabel),
            view.findViewById(R.id.tvTueLabel),
            view.findViewById(R.id.tvWedLabel),
            view.findViewById(R.id.tvThuLabel),
            view.findViewById(R.id.tvFriLabel),
            view.findViewById(R.id.tvSatLabel)
        )

        dayDates = listOf(
            view.findViewById(R.id.tvSunDate),
            view.findViewById(R.id.tvMonDate),
            view.findViewById(R.id.tvTueDate),
            view.findViewById(R.id.tvWedDate),
            view.findViewById(R.id.tvThuDate),
            view.findViewById(R.id.tvFriDate),
            view.findViewById(R.id.tvSatDate)
        )

        val dayNames = listOf(
            "Sunday, Oct 20",
            "Monday, Oct 21",
            "Tuesday, Oct 22",
            "Wednesday, Oct 23",
            "Thursday, Oct 24",
            "Friday, Oct 25",
            "Saturday, Oct 26"
        )

        dayViews.forEachIndexed { index, dayView ->
            dayView.setOnClickListener {
                animateButtonClick(dayView) {
                    selectDay(index, dayNames[index])
                }
            }
        }

        // Add New Booking Slot Button
        val btnAddSchedule = view.findViewById<View>(R.id.btnAddSchedule)
        btnAddSchedule.setOnClickListener {
            animateButtonClick(it) {
                showAddBookingDialog()
            }
        }

        // Action Buttons for Card 1
        val btnReschedule1 = view.findViewById<Button>(R.id.btnReschedule1)
        val btnCancel1 = view.findViewById<Button>(R.id.btnCancel1)
        val cardBooking1 = view.findViewById<View>(R.id.cardBooking1)

        btnReschedule1?.setOnClickListener {
            animateButtonClick(it) {
                Toast.makeText(requireContext(), "Reschedule requested for Alex Johnson", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel1?.setOnClickListener {
            animateButtonClick(it) {
                cardBooking1?.visibility = View.GONE
                Toast.makeText(requireContext(), "Booking with Alex Johnson cancelled", Toast.LENGTH_SHORT).show()
            }
        }

        // Action Buttons for Card 2
        val btnReschedule2 = view.findViewById<Button>(R.id.btnReschedule2)
        val btnCancel2 = view.findViewById<Button>(R.id.btnCancel2)
        val cardBooking2 = view.findViewById<View>(R.id.cardBooking2)

        btnReschedule2?.setOnClickListener {
            animateButtonClick(it) {
                Toast.makeText(requireContext(), "Reschedule requested for Sarah Jenkins", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel2?.setOnClickListener {
            animateButtonClick(it) {
                cardBooking2?.visibility = View.GONE
                Toast.makeText(requireContext(), "Booking with Sarah Jenkins cancelled", Toast.LENGTH_SHORT).show()
            }
        }

        // Action Buttons for Card 3
        val btnApproveBooking3 = view.findViewById<Button>(R.id.btnApproveBooking3)
        val btnDeclineBooking3 = view.findViewById<Button>(R.id.btnDeclineBooking3)
        val tvBadge3 = view.findViewById<TextView>(R.id.tvBadge3)
        val cardBooking3 = view.findViewById<View>(R.id.cardBooking3)

        btnApproveBooking3?.setOnClickListener {
            animateButtonClick(it) {
                tvBadge3?.text = "CONFIRMED"
                tvBadge3?.setBackgroundResource(R.drawable.bg_badge_green)
                Toast.makeText(requireContext(), "Booking for Michael Brown approved!", Toast.LENGTH_SHORT).show()
            }
        }

        btnDeclineBooking3?.setOnClickListener {
            animateButtonClick(it) {
                cardBooking3?.visibility = View.GONE
                Toast.makeText(requireContext(), "Booking for Michael Brown declined", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun selectDay(index: Int, dayName: String) {
        if (selectedDayIndex < dayViews.size) {
            dayViews[selectedDayIndex].setBackgroundResource(R.drawable.day_selector_inactive)
            dayLabels[selectedDayIndex].setTextColor(android.graphics.Color.parseColor("#9CA3AF"))
        }

        selectedDayIndex = index
        dayViews[index].setBackgroundResource(R.drawable.day_selector_active)
        dayLabels[index].setTextColor(android.graphics.Color.WHITE)

        tvDayHeader.text = "$dayName Bookings"
        val bookingCounts = listOf("1 Student Booking", "3 Student Bookings", "2 Student Bookings", "4 Student Bookings", "2 Student Bookings", "3 Student Bookings", "1 Student Booking")
        tvBookingCount.text = bookingCounts[index % bookingCounts.size]

        Toast.makeText(requireContext(), "Showing schedule for $dayName", Toast.LENGTH_SHORT).show()
    }

    private fun showAddBookingDialog() {
        val bottomSheet = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_booking, null)
        bottomSheet.setContentView(dialogView)

        val etStudentName = dialogView.findViewById<EditText>(R.id.etAddStudentName)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSaveBookingSlot)

        btnSave?.setOnClickListener {
            val studentName = etStudentName?.text?.toString()?.trim() ?: ""
            if (studentName.isNotEmpty()) {
                Toast.makeText(requireContext(), "Booking Slot created for $studentName!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "New Student Booking Slot created!", Toast.LENGTH_SHORT).show()
            }
            bottomSheet.dismiss()
        }

        bottomSheet.show()
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
