package com.dolla.mrimate.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dolla.mrimate.databinding.FragmentResultBinding
import com.dolla.mrimate.util.*


class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding // View binding
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
        binding = FragmentResultBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView() when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        binding.ivResult.setImageBitmap(deserializeBitmap(passedScanObj!!.scanImage))
        binding.tvResultClass.text = passedScanObj!!.classType
        binding.tvResultPrecetage.text = "Percentage: ${passedScanObj!!.percentage}%"
        if (passedScanObj!!.report.isNotEmpty())
            binding.tvResultReport.text = passedScanObj!!.report
        binding.tvResultDate.text = passedScanObj!!.date
        binding.tvResultReport.movementMethod = ScrollingMovementMethod()


        if (isDoctor) {
            if (passedScanObj!!.report.isEmpty())
                binding.tvResultReport.text =
                    "${binding.tvResultReport.text}, Click here to write a report"

            binding.tvResultReport.setOnClickListener {
                binding.tvResultReport.visibility = View.GONE
                binding.etResultReport.visibility = View.VISIBLE
                binding.etResultReport.requestFocus()
            }

            binding.etResultReport.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    binding.tvResultReport.visibility = View.VISIBLE
                    binding.etResultReport.visibility = View.GONE
                    binding.tvResultReport.text = binding.etResultReport.text


                    passedPatientObj!!.scans.indexOf(passedScanObj).let { index ->
                        passedPatientObj!!.scans[index].report =
                            binding.etResultReport.text.toString()
                    }
                    editPatientInFile(applicationContext!!, passedPatientObj!!)
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext = null // Clear the application context to avoid memory leaks
    }
}
