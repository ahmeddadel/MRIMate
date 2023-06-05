package com.dolla.mrimate.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.dolla.mrimate.databinding.ItemListBinding
import com.dolla.mrimate.pojo.Patient
import com.dolla.mrimate.util.deserializeBitmap

/**
 * @created 05/06/2023 - 11:54 AM
 * @project MRIMate
 * @author adell
 */

class PatientsAdapter :
    RecyclerView.Adapter<PatientsAdapter.PatientsViewHolder>() { // Adapter class for the RecyclerView in the ListFragment

    // lambda function that will be used to handle the click event on the Patient patient in the RecyclerView
    lateinit var onPatientClicked: (Patient) -> Unit // This function will be called when a Patient is clicked
    private var patientsList = ArrayList<Patient>() // This list will hold the list of patients

    fun setPatientsList(patientsList: ArrayList<Patient>) { // This function will be used to set the list of patients to the adapter and notify the adapter that the data set has changed
        this.patientsList = patientsList
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PatientsViewHolder { // This function will be called to create a new ViewHolder object instance and return it to the RecyclerView when it needs a new item view to display.
        // Initialize the view binding object instance using the layout file name
        return PatientsViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: PatientsViewHolder,
        position: Int
    ) { // This function will be called to bind the data to the view holder object instance

        // set animation to the view holder Patient item view (card view) when it is created for the first time only
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                android.R.anim.slide_in_left
            )
        )

        holder.binding.tvItemTitle.text =
            patientsList[position].name // Set the Patient Item name to the TextView
        holder.binding.tvItemSubtitle.text =
            patientsList[position].email // Set the Patient Item name to the TextView

        holder.itemView.setOnClickListener { // Set the click listener on the root view of the Patient item view in the RecyclerView (popular_patients.xml)
            onPatientClicked(patientsList[position]) // Call the lambda function to handle the click event on the Patient Item patient
        }
    }

    override fun getItemCount(): Int =
        patientsList.size // This function will return the number of patients in the list

    // ViewHolder class that takes in the view binding object instance of the popular_patients.xml layout file and extends the RecyclerView.ViewHolder class to hold the view binding object instance
    inner class PatientsViewHolder(val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) // ViewHolder class that takes in the view binding object instance of the popular_patients.xml layout file and extends the RecyclerView.ViewHolder class to hold the view binding object instance

}
