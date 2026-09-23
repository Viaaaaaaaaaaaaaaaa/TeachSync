package com.example.assistai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ChatbotFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chatbot, container, false)

        val etMessage = view.findViewById<EditText>(R.id.etMessage)
        val btnSend = view.findViewById<ImageButton>(R.id.btnSendMessage)
        val btnWeb = view.findViewById<ImageButton>(R.id.btnWeb)

        btnSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                etMessage.text.clear()
                Toast.makeText(requireContext(), "Message sent", Toast.LENGTH_SHORT).show()
            }
        }

        btnWeb.setOnClickListener {
            Toast.makeText(requireContext(), "Opening Web Search", Toast.LENGTH_SHORT).show()
        }

        val chipClasses = view.findViewById<TextView>(R.id.chipClasses)
        val chipBooking = view.findViewById<TextView>(R.id.chipBooking)
        val chipGmail = view.findViewById<TextView>(R.id.chipGmail)

        chipClasses.setOnClickListener { etMessage.setText(chipClasses.text) }
        chipBooking.setOnClickListener { etMessage.setText(chipBooking.text) }
        chipGmail.setOnClickListener { etMessage.setText(chipGmail.text) }

        return view
    }
}
