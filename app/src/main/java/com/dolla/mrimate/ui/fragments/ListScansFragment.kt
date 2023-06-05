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
import com.dolla.mrimate.adapters.ScansAdapter
import com.dolla.mrimate.databinding.FragmentListScansBinding
import com.dolla.mrimate.pojo.Patient
import com.dolla.mrimate.pojo.Scan
import com.dolla.mrimate.util.passedPatientObj
import com.dolla.mrimate.util.passedScanObj
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListScansFragment : Fragment() {

    private lateinit var binding: FragmentListScansBinding // View binding
    private var applicationContext: Context? = null// Application context
    private lateinit var scansAdapter: ScansAdapter // Scans adapter
    private lateinit var patient: Patient // Patient

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        applicationContext =
            requireActivity().applicationContext // get application context from the activity that is attached to the fragment

        scansAdapter = ScansAdapter() // Initialize the patients adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // This method is called to have the fragment instantiate its user interface view

        // Inflate the layout for this fragment
        binding = FragmentListScansBinding.inflate(inflater)

        patient = passedPatientObj!! // Get the patient from the passedPatientObj

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView() when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        prepareScansRecyclerView() // Prepare the RecyclerView for the patients
        observeScans() // Observe the patients
        onScanClicked() // Set the on click listener for the patient
    }

    private fun prepareScansRecyclerView() { // Prepare the RecyclerView for the categories
        binding.rvItems.apply { // Apply the following to the RecyclerView
            layoutManager = LinearLayoutManager(
                activity,
                GridLayoutManager.VERTICAL,
                false
            ) // Set the layout manager for the RecyclerView to a GridLayoutManager
            adapter = scansAdapter // Set the adapter for the RecyclerView
            setHasFixedSize(true) // Set the hasFixedSize property to true (improve performance)
        }
    }

    private fun observeScans() { // Observe the categories LiveData in the HomeViewModel
        onLoadingStart() // Show the loading animation
        lifecycleScope.launch(Dispatchers.IO) {

            delay(1000)

            val scans: List<Scan> = patient.scans

            val scansList = ArrayList<Scan>()
            scansList.addAll(scans)

            withContext(Dispatchers.Main) {
                if (scans.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.ivEmpty.visibility = View.VISIBLE
                    binding.rvItems.visibility = View.GONE
                }
                scansAdapter.setScansList(scansList) // Set the categories in the adapter
                onLoadingFinished() // Hide the loading animation
            }
        }
    }

    private fun onScanClicked() {
        scansAdapter.onScanClicked = { scan ->
            passedScanObj = scan // Set the passedScanObj to the scan that was clicked
            findNavController().navigate(ListScansFragmentDirections.actionListScansFragmentToResultFragment())
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