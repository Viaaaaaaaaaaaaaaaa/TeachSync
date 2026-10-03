package com.example.assistai

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvLinkedGmailSub: TextView
    private lateinit var tvProfileDetails: TextView
    private var imgProfileAvatar: ImageView? = null

    private var selectedImageUri: Uri? = null
    private var imgEditAvatarPreview: ImageView? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            imgEditAvatarPreview?.setImageURI(uri)
            imgProfileAvatar?.setImageURI(uri)
            Toast.makeText(requireContext(), "Profile photo selected from device", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvName = view.findViewById(R.id.tvProfileName)
        tvLinkedGmailSub = view.findViewById(R.id.tvLinkedGmailSub)
        tvProfileDetails = view.findViewById(R.id.tvProfileDetails)
        imgProfileAvatar = view.findViewById(R.id.imgProfileAvatar)

        val cardProfileHeader = view.findViewById<View>(R.id.cardProfileHeader)
        val cardPromoBanner = view.findViewById<View>(R.id.cardPromoBanner)

        val btnPersonalInfo = view.findViewById<View>(R.id.btnPersonalInfo)
        val btnLinkedGmail = view.findViewById<View>(R.id.btnLinkedGmail)
        val btnResetPassword = view.findViewById<View>(R.id.btnResetPassword)
        val btnNotifSettings = view.findViewById<View>(R.id.btnNotifSettings)
        val btnLogout = view.findViewById<View>(R.id.btnLogout)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userEmail = currentUser.email ?: "user@teachsync.ai"
            tvLinkedGmailSub.text = userEmail
            loadUserProfile(currentUser.uid)
        }

        cardProfileHeader?.setOnClickListener {
            animateButtonClick(it) {
                showEditProfileDialog()
            }
        }

        cardPromoBanner?.setOnClickListener {
            animateButtonClick(it) {
                (activity as? DashboardActivity)?.selectTab(R.id.nav_schedule)
            }
        }

        btnPersonalInfo?.setOnClickListener {
            animateButtonClick(it) {
                showEditProfileDialog()
            }
        }

        btnLinkedGmail?.setOnClickListener {
            animateButtonClick(it) {
                val intent = Intent(requireContext(), EmailMonitoringActivity::class.java)
                startActivity(intent)
            }
        }

        btnResetPassword?.setOnClickListener {
            animateButtonClick(it) {
                val intent = Intent(requireContext(), ForgotPasswordActivity::class.java)
                startActivity(intent)
            }
        }

        btnNotifSettings?.setOnClickListener {
            animateButtonClick(it) {
                (activity as? DashboardActivity)?.selectTab(R.id.nav_alerts)
            }
        }

        btnLogout?.setOnClickListener {
            animateButtonClick(it) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to sign out of TeachSync?")
                    .setPositiveButton("Log Out") { _, _ ->
                        auth.signOut()
                        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        activity?.finish()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }

        return view
    }

    private fun loadUserProfile(uid: String) {
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    val name = doc.getString("name") ?: "Judy"
                    val age = doc.getString("age") ?: "32"
                    val gender = doc.getString("gender") ?: "Female"
                    val job = doc.getString("job") ?: "Professor / Teacher"

                    tvName.text = name
                    tvProfileDetails.text = "Age: $age | Gender: $gender | Job: $job"
                }
            }
    }

    private fun showEditProfileDialog() {
        val bottomSheet = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)
        bottomSheet.setContentView(dialogView)

        val etEditName = dialogView.findViewById<EditText>(R.id.etEditName)
        val etEditAge = dialogView.findViewById<EditText>(R.id.etEditAge)
        val etEditJob = dialogView.findViewById<EditText>(R.id.etEditJob)
        val rbMale = dialogView.findViewById<RadioButton>(R.id.rbMale)
        val rbFemale = dialogView.findViewById<RadioButton>(R.id.rbFemale)
        val btnUploadPhotoVercel = dialogView.findViewById<View>(R.id.btnUploadPhotoVercel)
        val btnSaveProfile = dialogView.findViewById<Button>(R.id.btnSaveProfile)
        imgEditAvatarPreview = dialogView.findViewById<ImageView>(R.id.imgEditAvatar)

        if (selectedImageUri != null) {
            imgEditAvatarPreview?.setImageURI(selectedImageUri)
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: ""

        // Pre-fill existing info
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    etEditName?.setText(doc.getString("name") ?: "")
                    etEditAge?.setText(doc.getString("age") ?: "")
                    etEditJob?.setText(doc.getString("job") ?: "")
                    val gender = doc.getString("gender") ?: "Female"
                    if (gender.equals("Male", true)) {
                        rbMale?.isChecked = true
                    } else {
                        rbFemale?.isChecked = true
                    }
                }
            }

        btnUploadPhotoVercel?.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnSaveProfile?.setOnClickListener {
            val name = etEditName?.text?.toString()?.trim() ?: "Judy"
            val age = etEditAge?.text?.toString()?.trim() ?: "32"
            val job = etEditJob?.text?.toString()?.trim() ?: "Professor"
            val gender = if (rbMale?.isChecked == true) "Male" else "Female"

            val updates = hashMapOf<String, Any>(
                "name" to name,
                "age" to age,
                "gender" to gender,
                "job" to job
            )

            if (selectedImageUri != null) {
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(selectedImageUri!!)
                    val bytes = inputStream?.readBytes() ?: byteArrayOf()
                    VercelImageService.uploadProfilePhoto(uid, bytes) { photoUrl ->
                        if (photoUrl != null) {
                            updates["photoUrl"] = photoUrl
                        }
                    }
                } catch (e: Exception) {
                    // Ignore stream exception
                }
            }

            FirebaseFirestore.getInstance().collection("users").document(uid)
                .update(updates)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile updated & synced with Vercel successfully!", Toast.LENGTH_SHORT).show()
                    tvName.text = name
                    tvProfileDetails.text = "Age: $age | Gender: $gender | Job: $job"
                    bottomSheet.dismiss()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
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
