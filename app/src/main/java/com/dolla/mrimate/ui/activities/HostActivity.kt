package com.dolla.mrimate.ui.activities

import android.os.Bundle
import android.view.View.GONE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dolla.mrimate.R
import com.dolla.mrimate.databinding.ActivityHostBinding
import com.dolla.mrimate.util.setContentViewCustom
import com.google.android.material.bottomnavigation.BottomNavigationView

const val USER_IS_DOCTOR_KEY = "com.dolla.mrimate.user_is_doctor_key"

class HostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHostBinding
//    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the content view of the activity to the root of the layout
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentViewCustom(binding.root)

        // remove the bottom navigation view background color and disable the third item (middle item)
        binding.bottomNavView.background = null
        binding.bottomNavView.menu.getItem(2).isEnabled = false

        // set up the bottom navigation view with the nav controller
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        val navController = findNavController(R.id.nav_host_frag)
        bottomNavigationView.setupWithNavController(navController)

        binding.fab.setOnClickListener {
            Toast.makeText(applicationContext, "FAB clicked", Toast.LENGTH_SHORT).show()
        }

        binding.fab.visibility = GONE
        binding.bottomNavView.visibility = GONE
        binding.bottomAppBar.visibility = GONE

    }
}