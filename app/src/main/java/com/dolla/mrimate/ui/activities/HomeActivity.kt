package com.dolla.mrimate.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.dolla.mrimate.R
import com.dolla.mrimate.databinding.ActivityHomeBinding
import com.dolla.mrimate.util.isDoctor

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the content view of the activity to the root of the layout
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // remove the bottom navigation view background color and disable the third item (middle item)
        binding.bottomNavView.background = null
        binding.bottomNavView.menu.findItem(R.id.place_holder).isEnabled = false

        // set up the bottom navigation view with the nav controller
        navController = Navigation.findNavController(this, R.id.home_nav_host_fragment)
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

        binding.fabScan.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.listPatientsFragment -> navController.navigate(R.id.action_listPatientsFragment_to_scanFragment)
                R.id.listScansFragment -> navController.navigate(R.id.action_listScansFragment_to_scanFragment)
                R.id.resultFragment -> navController.navigate(R.id.action_resultFragment_to_scanFragment)
                R.id.savedFragment -> navController.navigate(R.id.action_savedFragment_to_scanFragment)
                R.id.aboutUsFragment -> navController.navigate(R.id.action_aboutUsFragment_to_scanFragment)
                R.id.settingsFragment -> navController.navigate(R.id.action_settingsFragment_to_scanFragment)
            }
            // set the selected item in the bottom navigation view to the scan fragment
            binding.bottomNavView.selectedItemId = R.id.place_holder
        }
        onNavigationItemSelected()
    }

    private fun onNavigationItemSelected() {
        binding.bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.savedFragment -> {
                    navController.popBackStack(R.id.savedFragment, true)
                    navController.navigate(R.id.savedFragment)
                }
                R.id.aboutUsFragment -> {
                    navController.popBackStack(R.id.aboutUsFragment, true)
                    navController.navigate(R.id.aboutUsFragment)
                }
                R.id.settingsFragment -> {
                    navController.popBackStack(R.id.settingsFragment, true)
                    navController.navigate(R.id.settingsFragment)
                }
                R.id.listPatientsFragment -> {
                    if (isDoctor) {
                        navController.popBackStack(R.id.listPatientsFragment, true)
                        navController.navigate(R.id.listPatientsFragment)
                    } else {
                        navController.popBackStack(R.id.listScansFragment, true)
                        navController.navigate(R.id.listScansFragment)
                    }
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.scanFragment) { // if we are in the scan fragment
            navController.navigate(R.id.action_scanFragment_to_listPatientsFragment) // navigate to the home fragment
            navController.popBackStack() // pop the back stack
        } else if (navController.currentDestination?.id == R.id.scanFragment && !isDoctor) { // if we are in the scan fragment
            navController.navigate(R.id.action_scanFragment_to_listScansFragment) // navigate to the home fragment
            navController.popBackStack() // pop the back stack
        } else if (navController.currentDestination?.id == R.id.listScansFragment && !isDoctor) // if we are in the home fragment
            finish() // finish the activity
        else
            super.onBackPressed() // else, do the default action
    }
}