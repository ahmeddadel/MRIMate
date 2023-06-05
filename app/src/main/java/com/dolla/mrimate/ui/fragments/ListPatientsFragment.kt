package com.dolla.mrimate.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dolla.mrimate.R
import com.dolla.mrimate.adapters.PatientsAdapter
import com.dolla.mrimate.databinding.FragmentListPatientsBinding
import com.dolla.mrimate.pojo.Patient
import com.dolla.mrimate.util.*
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListPatientsFragment : Fragment() {

    private lateinit var binding: FragmentListPatientsBinding // View binding
    private var applicationContext: Context? = null// Application context
    private lateinit var patientsAdapter: PatientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        applicationContext =
            requireActivity().applicationContext // get application context from the activity that is attached to the fragment

        patientsAdapter = PatientsAdapter() // Initialize the patients adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // This method is called to have the fragment instantiate its user interface view

        // Inflate the layout for this fragment
        binding = FragmentListPatientsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView() when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        if (isDoctor && doctorObj != null) {
            preparePatientsRecyclerView() // Prepare the RecyclerView for the patients
            observePatients() // Observe the patients
            onPatientClicked() // Set the on click listener for the patient
        } else if (!isDoctor && patientObj != null) {
            passedPatientObj = patientObj
            findNavController().navigate(
                ListPatientsFragmentDirections.actionListPatientsFragmentToListScansFragment(
                )
            )
        }
    }

    private fun preparePatientsRecyclerView() { // Prepare the RecyclerView for the categories
        binding.rvItems.apply { // Apply the following to the RecyclerView
            layoutManager = LinearLayoutManager(
                activity,
                GridLayoutManager.VERTICAL,
                false
            ) // Set the layout manager for the RecyclerView to a GridLayoutManager
            adapter = patientsAdapter // Set the adapter for the RecyclerView
            setHasFixedSize(true) // Set the hasFixedSize property to true (improve performance)
        }
    }

    private fun observePatients() { // Observe the categories LiveData in the HomeViewModel
        if (isDoctor) {
            onLoadingStart() // Show the loading animation
            lifecycleScope.launch(Dispatchers.IO) {

                delay(1000)

                val patients =
                    readPatientsFromFile(applicationContext!!) // Read the categories from the JSON file
                val patientsList = ArrayList<Patient>()
                patientsList.addAll(patients)

                withContext(Dispatchers.Main) {
                    patientsAdapter.setPatientsList(patientsList) // Set the categories in the adapter
                    onLoadingFinished() // Hide the loading animation
                }
            }
        }
    }

    private fun onPatientClicked() {
        patientsAdapter.onPatientClicked = { patient ->
            passedPatientObj = patient
            findNavController().navigate(
                ListPatientsFragmentDirections.actionListPatientsFragmentToListScansFragment(
                )
            )
        }
    }

    private fun onLoadingStart() {
        binding.progressBar.visibility = View.VISIBLE // Show the progress bar
        binding.rvItems.visibility = View.GONE // Hide the RecyclerView
    }

    private fun onLoadingFinished() {
        binding.progressBar.visibility = View.GONE // Hide the progress bar
        binding.rvItems.visibility = View.VISIBLE // Show the RecyclerView
    }

    private fun showToastMessage(message: String) {
        Toasty.custom(
            applicationContext!!,
            message,
            R.drawable.ic_logo,
            R.color.colorPrimaryVariant,
            Toast.LENGTH_LONG,
            true,
            true
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext = null // Clear the application context to avoid memory leaks
    }
}