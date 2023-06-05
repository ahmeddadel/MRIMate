package com.dolla.mrimate.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dolla.mrimate.databinding.FragmentSettingsBinding
import com.dolla.mrimate.util.doctorObj
import com.dolla.mrimate.util.isDoctor
import com.dolla.mrimate.util.patientObj

class SettingsFragment : Fragment() {


    private lateinit var binding: FragmentSettingsBinding // View binding
    private var applicationContext: Context? = null// Application context

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        applicationContext =
            requireActivity().applicationContext // get application context from the activity that is attached to the fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // This method is called to have the fragment instantiate its user interface view

        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView() when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        if (isDoctor) {
            binding.tvName.text = "Dr. ${doctorObj!!.name}"
            binding.tvEmail.text = doctorObj!!.email
            binding.tvHospitalId.text = "ID: ${doctorObj!!.hospitalId}"
        } else {
            binding.tvName.text = "Mr. ${patientObj!!.name}"
            binding.tvEmail.text = patientObj!!.email
            binding.tvHospitalId.visibility = View.GONE
        }
    }
}
