package com.example.assistai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

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
        val bookingCounts = listOf("1 Real-Time Booking", "3 Real-Time Bookings", "2 Real-Time Bookings", "4 Real-Time Bookings", "2 Real-Time Bookings", "3 Real-Time Bookings", "1 Real-Time Booking")
        tvBookingCount.text = bookingCounts[index % bookingCounts.size]

        Toast.makeText(requireContext(), "Showing schedule for $dayName", Toast.LENGTH_SHORT).show()
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
