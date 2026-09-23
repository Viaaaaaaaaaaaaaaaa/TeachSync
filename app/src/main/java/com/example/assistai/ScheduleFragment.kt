package com.example.assistai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment

class ScheduleFragment : Fragment() {

    private var selectedDayView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)

        val days = listOf(
            view.findViewById<LinearLayout>(R.id.daySun),
            view.findViewById<LinearLayout>(R.id.dayMon),
            view.findViewById<LinearLayout>(R.id.dayTue),
            view.findViewById<LinearLayout>(R.id.dayWed),
            view.findViewById<LinearLayout>(R.id.dayThu),
            view.findViewById<LinearLayout>(R.id.dayFri),
            view.findViewById<LinearLayout>(R.id.daySat)
        )

        selectedDayView = days[0] // Sunday is selected by default in XML

        val dayNames = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

        days.forEachIndexed { index, dayView ->
            dayView.setOnClickListener {
                selectDay(dayView)
                Toast.makeText(requireContext(), "Filtering for ${dayNames[index]}", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<ImageButton>(R.id.btnAddSchedule).setOnClickListener {
            Toast.makeText(requireContext(), "Add schedule clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btnAllInstructors).setOnClickListener {
            Toast.makeText(requireContext(), "Showing all instructors", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btnEditYoga).setOnClickListener {
            Toast.makeText(requireContext(), "Edit Yoga Session", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btnDeleteYoga).setOnClickListener {
            Toast.makeText(requireContext(), "Delete Yoga Session", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btnEditPilates).setOnClickListener {
            Toast.makeText(requireContext(), "Edit Pilates Beginner", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btnDeletePilates).setOnClickListener {
            Toast.makeText(requireContext(), "Delete Pilates Beginner", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun selectDay(view: View) {
        selectedDayView?.setBackgroundResource(0) // Remove highlight
        view.setBackgroundResource(R.drawable.day_selector_bg)
        selectedDayView = view
    }
}
