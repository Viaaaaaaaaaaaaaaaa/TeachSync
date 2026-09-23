package com.example.assistai

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val bottomNav = findViewById<BottomNavigationView>(R.id.adminBottomNav)

        if (savedInstanceState == null) {
            loadFragment(AdminHomeFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_admin_home -> AdminHomeFragment()
                R.id.nav_admin_approvals -> AdminApprovalsFragment()
                R.id.nav_admin_schedule -> ScheduleFragment()
                R.id.nav_admin_chatbot -> ChatbotFragment()
                else -> AdminHomeFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    fun selectTab(itemId: Int) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.adminBottomNav)
        bottomNav.selectedItemId = itemId
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.adminFragmentContainer, fragment)
            .commit()
    }
}
